package by.tms.Diplom.Repository;

import by.tms.Diplom.Entity.Item;
import by.tms.Diplom.Entity.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findItemBySerialNumber(String serialNumber);
    boolean existsItemBySerialNumber(String serialNumber);
    Item findItemById(long id);
    boolean existsItemById(long id);
    List<Item> findItemsByCategoryNameAndItemStatus(String name, ItemStatus itemStatus);
    List<Item> findItemsByModelAndItemStatusAndCategoryName(String model, ItemStatus itemStatus, String name);
    boolean existsItemsByModelAndItemStatusAndCategoryName(String model, ItemStatus itemStatus, String name);
    List<Item> findItemsByItemStatus(ItemStatus itemStatus);
    boolean existsItemsByItemStatus(ItemStatus itemStatus);
}
