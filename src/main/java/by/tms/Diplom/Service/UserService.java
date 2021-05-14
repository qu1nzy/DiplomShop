package by.tms.Diplom.Service;

import by.tms.Diplom.Entity.User;
import by.tms.Diplom.Entity.UserRole;
import by.tms.Diplom.Repository.UserRepository;
import by.tms.Diplom.Service.Exception.AdminExistException;
import by.tms.Diplom.Service.Exception.UserAlreadyExistsException;
import by.tms.Diplom.Service.Exception.UserNotFoundException;
import by.tms.Diplom.Service.Exception.UsersNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static int adminTemp = 0;

    public int getAdminTemp() {
        int newAdminTemp = adminTemp;
        log.info("userService, getAdminTemp - success");
        return newAdminTemp;
    }

    public void addFirstAdmin(User user) {
        if (userRepository.findAll().size() == 0) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setWallet("0");
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);
            adminTemp++;
            log.info("userService, addFirstAdmin - success");
        } else {
            log.info("userService, AdminExistException - success");
            throw new AdminExistException("Admin is exist");
        }
    }

    public void addUser(User user) {
        if (userRepository.existsUserByLogin(user.getLogin())) {
            log.info("userService, UserAlreadyExistsException - success");
            throw new UserAlreadyExistsException("User is exist");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setWallet("0");
            user.setUserRole(UserRole.USER);
            userRepository.save(user);
            log.info("userService, addUser - success");
        }
    }

    public void updatePasswordByLogin(String login, String newPassword) {
        if (userRepository.existsUserByLogin(login)) {
            String encode = passwordEncoder.encode(newPassword);
            User user = userRepository.findUserByLogin(login);
            user.setPassword(encode);
            userRepository.save(user);
            log.info("userService, updatePasswordByLogin - success");
        } else {
            log.info("userService, UserNotFoundException - success");
            throw new UserNotFoundException("User is not found");
        }
    }

    public void topUpUserWalletById(String money, long id) {
        if (userRepository.existsUserById(id)) {
            User user = userRepository.findUserById(id);
            double tempWallet = Double.parseDouble(user.getWallet());
            double tempMoney = Double.parseDouble(money);
            double newWallet = tempWallet + tempMoney;
            user.setWallet(Double.toString(newWallet));
            userRepository.save(user);
            log.info("userService, topUpWalletById - success");
        } else {
            log.info("userService, UserNotFoundException - success");
            throw new UserNotFoundException("User is not found");
        }
    }

    public List<User> findAllUsersForAdmin() {
        log.info("userService, findAllUsersForAdmin - success");
        return userRepository.findAll();
    }

    public List<User> findAllUsersForManager() {
        if (userRepository.existsUserByUserRole(UserRole.USER)) {
            log.info("userService, findAllUsersForManager - success");
            return userRepository.findUsersByUserRole(UserRole.USER);
        } else {
            log.info("userService, UsersNotFoundException - success");
            throw new UsersNotFoundException("Users are not found");
        }
    }

    public User findUserByIdForAdmin(long id) {
        if (userRepository.existsUserById(id)) {
            log.info("userService, findUserByIdForAdmin - success");
            return userRepository.findUserById(id);
        } else {
            log.info("userService, UserNotFoundException - success");
            throw new UserNotFoundException("User is not found");
        }
    }

    public User findUserByIdForManager(long id) {
        if (userRepository.existsUserById(id) && userRepository.findUserById(id).getUserRole().equals(UserRole.USER)) {
            log.info("userService, findUserByIdForManager - success");
            return userRepository.findUserById(id);
        } else {
            log.info("userService, UserNotFoundException - success");
            throw new UserNotFoundException("User is not found");
        }
    }

    public boolean existsUserByLogin(String login) {
        log.info("userService, existsUserByLogin - success");
        return userRepository.existsUserByLogin(login);
    }

    public void updateUserRoleById(long id, UserRole newUserRole) {
        if (userRepository.existsUserById(id)) {
            User user = userRepository.findUserById(id);
            user.setUserRole(newUserRole);
            userRepository.save(user);
            log.info("userService, updateUserRoleById - success");
        } else {
            log.info("userService, UserNotFoundException - success");
            throw new UserNotFoundException("User is not found");
        }
    }

    public User findUserByLogin(String login) {
        if (userRepository.existsUserByLogin(login)) {
            log.info("userService, findUserByLogin - success");
            return userRepository.findUserByLogin(login);
        } else {
            log.info("userService, UserNotFoundException - success");
            throw new UserNotFoundException("User is not found");
        }
    }

    public List<User> findUsersByUserRole(UserRole userRole) {
        if (userRepository.existsUserByUserRole(userRole)) {
            log.info("userService, findUsersByUserRole - success");
            return userRepository.findUsersByUserRole(userRole);
        } else {
            log.info("userService, UsersNotFoundException - success");
            throw new UsersNotFoundException("Users are not found");
        }
    }
}
