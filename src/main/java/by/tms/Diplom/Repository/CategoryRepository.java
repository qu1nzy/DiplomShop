package by.tms.Diplom.Repository;
import by.tms.Diplom.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsCategoryByName(String categoryName);
    Category findCategoryByName(String categoryName);
    Category findCategoryById(long id);
    boolean existsCategoryById(long id);
}