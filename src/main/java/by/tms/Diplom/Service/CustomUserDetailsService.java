package by.tms.Diplom.Service;

import by.tms.Diplom.Entity.User;
import by.tms.Diplom.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if(userRepository.existsUserByLogin(login)){
            User user = userRepository.findUserByLogin(login);
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getLogin())
                    .password(user.getPassword())
                    .authorities(user.getUserRole())
                    .build();
            log.info("customUserDetailsService, loadUserByUsername - success");
            return userDetails;
        } else {
            log.info("customUserDetailsService, UsernameNotFoundException - success");
            throw new UsernameNotFoundException("User is not found");
        }
    }
}
