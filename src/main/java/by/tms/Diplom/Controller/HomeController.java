package by.tms.Diplom.Controller;

import by.tms.Diplom.Entity.UserRole;
import by.tms.Diplom.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/")
@Slf4j
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView homePage(ModelAndView modelAndView) {
        modelAndView.addObject("adminTemp", userService.getAdminTemp());
        modelAndView.setViewName("home");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int markerUserRole;
        if(userService.existsUserByLogin(authentication.getName())) {
            modelAndView.addObject("marker", true);
            modelAndView.addObject("wallet", userService.findUserByLogin(authentication.getName()).getWallet());
            if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN)) {
                markerUserRole = 1;
                modelAndView.addObject("userRole", markerUserRole);
                log.info("homeController, homePage(ADMIN) - success");
            } else if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
                markerUserRole = 2;
                modelAndView.addObject("userRole", markerUserRole);
                log.info("homeController, homePage(MANAGER) - success");
            } else {
                markerUserRole = 3;
                modelAndView.addObject("userRole", markerUserRole);
                log.info("homeController, homePage(USER) - success");
            }
        } else {
            modelAndView.addObject("marker", false);
            log.info("homeController, homePage(Anonymous) - success");
        }
        return modelAndView;
    }
}
