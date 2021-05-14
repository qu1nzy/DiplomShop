package by.tms.Diplom.Controller;

import by.tms.Diplom.Service.Exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(AdminExistException.class)
    public String adminExistException(AdminExistException adminExistException, Model model) {
        model.addAttribute("message", adminExistException.getMessage());
        log.info("exceptionController, adminExistException - success");
        return "firstAdminException";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String userIsExistException(UserAlreadyExistsException userAlreadyExistsException, Model model) {
        model.addAttribute("message", userAlreadyExistsException.getMessage());
        log.info("exceptionController, userIsExistException - success");
        return "userIsExistException";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String userIsNotFoundException(UserNotFoundException userNotFoundException, Model model) {
        model.addAttribute("message", userNotFoundException.getMessage());
        log.info("exceptionController, userIsNotFoundException - success");
        return "userIsNotFoundException";
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public String categoryIsExistException(CategoryAlreadyExistsException categoryAlreadyExistsException, Model model) {
        model.addAttribute("message", categoryAlreadyExistsException.getMessage());
        log.info("exceptionController, categoryIsExistException - success");
        return "categoryExistException";
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public String categoryIsNotFoundException(CategoryNotFoundException categoryNotFoundException, Model model) {
        model.addAttribute("message", categoryNotFoundException.getMessage());
        log.info("exceptionController, categoryIsNotFoundException - success");
        return "categoryIsNotFoundException";
    }

    @ExceptionHandler(CategoriesNotFoundException.class)
    public String categoriesIsNotFoundException(CategoriesNotFoundException categoriesNotFoundException, Model model) {
        model.addAttribute("message", categoriesNotFoundException.getMessage());
        log.info("exceptionController, categoriesIsNotFoundException - success");
        return "categoriesIsNotFoundException";
    }

    @ExceptionHandler(ItemsNotFoundException.class)
    public String itemsIsNotFoundException(ItemsNotFoundException itemsNotFoundException, Model model) {
        model.addAttribute("message", itemsNotFoundException.getMessage());
        log.info("exceptionController, itemsIsNotFoundException - success");
        return "itemsIsNotFoundException";
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public String itemIsNotFoundException(ItemNotFoundException itemNotFoundException, Model model) {
        model.addAttribute("message", itemNotFoundException.getMessage());
        log.info("exceptionController, itemIsNotFoundException - success");
        return "itemIsNotFoundException";
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    public String itemIsExistException(ItemAlreadyExistsException itemAlreadyExistsException, Model model) {
        model.addAttribute("message", itemAlreadyExistsException.getMessage());
        log.info("exceptionController, itemIsExistException - success");
        return "itemIsExistException";
    }

    @ExceptionHandler(UsersNotFoundException.class)
    public String usersNotFoundException(UsersNotFoundException usersNotFoundException, Model model) {
        model.addAttribute("message", usersNotFoundException.getMessage());
        log.info("exceptionController, usersNotFoundException - success");
        return "usersIsNotFoundException";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String orderIsNotFoundException(OrderNotFoundException orderNotFoundException, Model model) {
        model.addAttribute("message", orderNotFoundException.getMessage());
        log.info("exceptionController, orderIsNotFoundException - success");
        return "orderIsNotFoundException";
    }

    @ExceptionHandler(OrdersNotFoundException.class)
    public String ordersIsNotFoundException(OrdersNotFoundException ordersNotFoundException, Model model) {
        model.addAttribute("message", ordersNotFoundException.getMessage());
        log.info("exceptionController, ordersIsNotFoundException - success");
        return "ordersIsNotFoundException";
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public String notEnoughFundsException(NotEnoughMoneyException notEnoughMoneyException, Model model) {
        model.addAttribute("message", notEnoughMoneyException.getMessage());
        log.info("exceptionController, notEnoughFundsException - success");
        return "notEnoughFundsException";
    }
}