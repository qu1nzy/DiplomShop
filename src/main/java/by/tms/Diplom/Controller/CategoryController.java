package by.tms.Diplom.Controller;

import by.tms.Diplom.Entity.Category;
import by.tms.Diplom.Entity.CategoryAdd;
import by.tms.Diplom.Entity.ItemStatus;
import by.tms.Diplom.Entity.UserRole;
import by.tms.Diplom.Service.CategoryService;
import by.tms.Diplom.Service.ItemService;
import by.tms.Diplom.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @GetMapping("add")
    public ModelAndView addCategoryGet(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN) || userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.addObject("category", new CategoryAdd());
            modelAndView.setViewName("categoryAdd");
            log.info("categoryController, addCategoryGet(ADMIN or MANAGER) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("categoryController, addCategoryGet(User) - success");
        }
        return modelAndView;
    }

    @PostMapping("add")
    public ModelAndView addCategoryPost(@Valid @ModelAttribute("category") CategoryAdd categoryAdd, Errors errors, ModelAndView modelAndView) {
        int marker;
        if (errors.hasErrors()) {
            modelAndView.setViewName("categoryAdd");
            log.info("categoryController, addCategoryPost(Errors) - success");
        } else {
            Category category = new Category();
            category.setName(categoryAdd.getName());
            categoryService.addCategory(category);
            marker = 1;
            modelAndView.addObject("marker", marker);
            modelAndView.setViewName("categoryAdd");
            log.info("categoryController, addCategoryPost - success");
        }
        return modelAndView;
    }

    @GetMapping("categories")
    public ModelAndView categoriesGet(ModelAndView modelAndView) {
        modelAndView.addObject("categories", categoryService.getCategories());
        modelAndView.setViewName("categories");
        log.info("categoryController, categoriesGet - success");
        return modelAndView;
    }

    @GetMapping("categories/{id}")
    public ModelAndView getCategoryView(@PathVariable("id") long id, ModelAndView modelAndView) {
        Category category = categoryService.findCategoryById(id);
        modelAndView.addObject("category", category);
        modelAndView.addObject("items", itemService.findItemsByCategoryNameAndItemStatus(category.getName(), ItemStatus.ACTIVE));
        modelAndView.setViewName("itemsByCategory");
        log.info("categoryController, getCategoryView - success");
        return modelAndView;
    }
}

