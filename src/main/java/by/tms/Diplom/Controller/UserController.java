package by.tms.Diplom.Controller;

import by.tms.Diplom.Entity.*;
import by.tms.Diplom.Service.Exception.UserNotFoundException;
import by.tms.Diplom.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("addFirstAdmin")
    public ModelAndView addFirstAdminGet(ModelAndView modelAndView) {
        modelAndView.setViewName("addFirstAdmin");
        modelAndView.addObject("admin", new UserModel());
        log.info("userController, addFirstAdminGet - success");
        return modelAndView;
    }

    @PostMapping("addFirstAdmin")
    public ModelAndView addFirstAdminPost(@Valid @ModelAttribute("admin") UserModel userModel, Errors errors, ModelAndView modelAndView) {
        if (errors.hasErrors()) {
            modelAndView.setViewName("addFirstAdmin");
            log.info("userController, addFirstAdminPost(Error) - success");
        } else {
            User user = new User();
            user.setPassword(userModel.getPassword());
            user.setLogin(userModel.getLogin());
            user.setName(userModel.getName());
            modelAndView.setViewName("redirect:/");
            userService.addFirstAdmin(user);
            log.info("userController, addFirstAdminPost - success");
        }
        return modelAndView;
    }

    @GetMapping("registration")
    public ModelAndView registrationNewUserGet(ModelAndView modelAndView) {
        modelAndView.addObject("user", new UserModel());
        modelAndView.setViewName("registration");
        log.info("userController, registrationNewUserGet - success");
        return modelAndView;
    }

    @PostMapping("registration")
    public ModelAndView registrationNewUserPost(@Valid @ModelAttribute("user") UserModel userModel, Errors errors, ModelAndView modelAndView) {
        int marker;
        if (errors.hasErrors()) {
            modelAndView.setViewName("registration");
            log.info("userController, registrationNewUserPost(Error) - success");
        } else {
            User user = new User();
            user.setPassword(userModel.getPassword());
            user.setLogin(userModel.getLogin());
            user.setName(userModel.getName());
            modelAndView.setViewName("registration");
            userService.addUser(user);
            marker = 1;
            modelAndView.addObject("marker", marker);
            log.info("userController, registrationNewUserPost - success");
        }
        return modelAndView;
    }

    @GetMapping("authentication")
    public ModelAndView authenticationUserGet(ModelAndView modelAndView) {
        modelAndView.setViewName("authentication");
        log.info("userController, authenticationUserGet - success");
        return modelAndView;
    }

    @GetMapping("allUsers")
    public ModelAndView allUsersGet(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN)) {
            modelAndView.addObject("allUsers", userService.findAllUsersForAdmin());
            modelAndView.setViewName("allUsers");
            log.info("userController, allUsersGet(ADMIN) - success");
        } else if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.addObject("allUsers", userService.findAllUsersForManager());
            modelAndView.setViewName("allUsers");
            log.info("userController, allUsersGet(MANAGER) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("userController, allUsersGet(USER) - success");
        }
        return modelAndView;
    }

    @GetMapping("updatePassword")
    public ModelAndView updatePasswordGet(ModelAndView modelAndView){
        modelAndView.addObject("newPassword", new UserPassword());
        modelAndView.setViewName("updatePassword");
        log.info("userController, updatePasswordGet - success");
        return modelAndView;
    }

    @PostMapping("updatePassword")
    public ModelAndView updatePasswordPost(@Valid @ModelAttribute("newPassword") UserPassword userPassword, Errors errors, ModelAndView modelAndView, HttpSession session) {
        if (errors.hasErrors()) {
            modelAndView.setViewName("updatePassword");
            log.info("userController, updatePasswordPost(Error) - success");
        } else {
            modelAndView.setViewName("redirect:/user/authentication");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userService.updatePasswordByLogin(userService.findUserByLogin(authentication.getName()).getLogin(), userPassword.getPassword());
            session.invalidate();
            log.info("userController, updatePasswordPost - success");
        }
        return modelAndView;
    }

    @GetMapping("wallet")
    public ModelAndView upWalletGet(ModelAndView modelAndView) {
        modelAndView.addObject("wallet", new UserWallet());
        modelAndView.setViewName("wallet");
        log.info("userController, upWalletGet - success");
        return modelAndView;
    }

    @PostMapping("wallet")
    public ModelAndView upWalletPost(@Valid @ModelAttribute("wallet") UserWallet userWallet,Errors errors, ModelAndView modelAndView) {
        int marker;
        if (errors.hasErrors()) {
            modelAndView.setViewName("wallet");
            log.info("userController, upWalletPost(Error) - success");
        } else {
            Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
            marker = 1;
            modelAndView.addObject("marker", marker);
            modelAndView.setViewName("wallet");
            userService.topUpUserWalletById(userWallet.getWallet(), userService.findUserByLogin(authentication.getName()).getId());
            log.info("userController, upWalletPost - success");
        }
        return modelAndView;
    }

    @GetMapping("userId")
    public ModelAndView getUserById(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN)) {
            modelAndView.addObject("id", new UserId());
            modelAndView.setViewName("userId");
            log.info("userController, getUserById(ADMIN) - success");
        } else if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.addObject("id", new UserId());
            modelAndView.setViewName("userId");
            log.info("userController, getUserById(MANAGER) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("userController, getUserById(USER) - success");
        }
        return modelAndView;
    }

    @PostMapping("userId")
    public ModelAndView postUserById(@Valid @ModelAttribute("id") UserId userId, Errors errors, ModelAndView modelAndView) {
        if (errors.hasErrors()) {
            modelAndView.setViewName("userId");
            log.info("userController, postUserById(Error) - success");
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN)) {
                modelAndView.addObject("user", userService.findUserByIdForAdmin(Long.parseLong(userId.getId())));
                modelAndView.setViewName("user");
                log.info("userController, postUserById(ADMIN) - success");
            } else if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
                modelAndView.addObject("user", userService.findUserByIdForManager(Long.parseLong(userId.getId())));
                modelAndView.setViewName("user");
                log.info("userController, postUserById(MANAGER) - success");
            }
        }
        return modelAndView;
    }

    @GetMapping("role")
    public ModelAndView getUpdateRole(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN)) {
            modelAndView.addObject("id", new UserId());
            modelAndView.setViewName("userRole");
            log.info("userController, getUpdateRole(ADMIN) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("userController, getUpdateRole(MANAGER or USER) - success");
        }
        return modelAndView;
    }

    @PostMapping("role")
    public ModelAndView postUpdateRole(@Valid @ModelAttribute("id") UserId userId, Errors errors, UserRole userRole, ModelAndView modelAndView) {
        int marker;
        if (errors.hasErrors()) {
            modelAndView.setViewName("userRole");
            log.info("userController, postUpdateRole(Error) - success");
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<User> users = userService.findUsersByUserRole(UserRole.ADMIN);
            User user = userService.findUserByIdForAdmin(Long.parseLong(userId.getId()));
            if(userService.findUserByLogin(authentication.getName()).getId() == user.getId() && users.size() == 1) {
                log.info("userController, UserNotFoundException - success");
                throw new UserNotFoundException("An admin cannot change his role as long as he is the only admin");
            } else {
                userService.updateUserRoleById(Long.parseLong(userId.getId()), userRole);
                marker = 1;
                modelAndView.addObject("marker", marker);
                modelAndView.setViewName("userRole");
                log.info("userController, postUpdateRole - success");
            }
        }
        return modelAndView;
    }
}