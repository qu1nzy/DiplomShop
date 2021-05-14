package by.tms.Diplom.Service;


import by.tms.Diplom.Entity.Category;
import by.tms.Diplom.Repository.CategoryRepository;
import by.tms.Diplom.Service.Exception.CategoriesNotFoundException;
import by.tms.Diplom.Service.Exception.CategoryAlreadyExistsException;
import by.tms.Diplom.Service.Exception.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void addCategory(Category newCategory) {
        if (categoryRepository.findAll().size() == 0) {
            categoryRepository.save(newCategory);
            log.info("categoryService, addCategory - success");
        } else {
            if (categoryRepository.existsCategoryByName(newCategory.getName())) {
                log.info("categoryService, CategoryAlreadyExistsException - success");
                throw new CategoryAlreadyExistsException("Category is exists");
            } else {
                categoryRepository.save(newCategory);
                log.info("categoryService, addCategory - success");
            }
        }
    }

    public List<Category> getCategories() {
        if (categoryRepository.findAll().size() != 0) {
            log.info("categoryService, getCategories - success");
            return categoryRepository.findAll();
        } else {
            log.info("categoryService, CategoriesNotFoundException - success");
            throw new CategoriesNotFoundException("Categories are not found");
        }
    }

    public Category findCategoryByName(String name) {
        if (categoryRepository.existsCategoryByName(name)) {
            log.info("categoryService, findCategoryByName - success");
            return categoryRepository.findCategoryByName(name);
        } else {
            log.info("categoryService, CategoryNotFoundException - success");
            throw new CategoryNotFoundException("Category is not found");
        }
    }

    public boolean existCategoryByName(String name) {
        log.info("categoryService, existCategoryByName - success");
        return categoryRepository.existsCategoryByName(name);
    }

    public Category findCategoryById(long id) {
        if(categoryRepository.existsCategoryById(id)) {
            log.info("categoryService, findCategoryById - success");
            return categoryRepository.findCategoryById(id);
        } else {
            log.info("categoryService, CategoryNotFoundException - success");
            throw new CategoryNotFoundException("Category is not found");
        }
    }
}
