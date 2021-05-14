package by.tms.Diplom.Service;

import by.tms.Diplom.Entity.Item;
import by.tms.Diplom.Entity.ItemStatus;
import by.tms.Diplom.Entity.Order;
import by.tms.Diplom.Repository.ItemRepository;
import by.tms.Diplom.Service.Exception.ItemAlreadyExistsException;
import by.tms.Diplom.Service.Exception.ItemNotFoundException;
import by.tms.Diplom.Service.Exception.ItemsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public void addItem(Item newItem) {
        if (itemRepository.findAll().size() == 0) {
            itemRepository.save(newItem);
            log.info("itemService, addItem - success");
        } else {
            if (itemRepository.existsItemBySerialNumber(newItem.getSerialNumber())) {
                log.info("itemService, ItemAlreadyExistsException - success");
                throw new ItemAlreadyExistsException("Item is exists");
            } else {
                itemRepository.save(newItem);
                log.info("itemService, addItem - success");
            }
        }
    }

    public List<Item> findItemsByCategoryNameAndItemStatus(String categoryName, ItemStatus itemStatus) {
        if (itemRepository.findAll().size() != 0) {
            if (itemRepository.findItemsByCategoryNameAndItemStatus(categoryName, ItemStatus.ACTIVE).size() != 0) {
                List<Item> items = itemRepository.findItemsByCategoryNameAndItemStatus(categoryName, itemStatus);
                Set<Item> itemSet = new HashSet<>(items);
                items.clear();
                items.addAll(itemSet);
                log.info("itemService, findItemsByCategoryNameAndItemStatus - success");
                return items;
            } else {
                log.info("itemService, ItemsNotFoundException - success");
                throw new ItemsNotFoundException("Items with this category name are not found");
            }
        } else {
            log.info("itemService, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Items are not found");
        }
    }

    public int numbersOfItemsByModelAndItemStatusAndCategoryName(String model, ItemStatus itemStatus, String name) {
        if (itemRepository.findAll().size() != 0) {
            if (itemRepository.existsItemsByModelAndItemStatusAndCategoryName(model, itemStatus, name)) {
                log.info("itemService, numbersOfItemsByModelAndItemStatusAndCategoryName - success");
                return itemRepository.findItemsByModelAndItemStatusAndCategoryName(model, itemStatus, name).size();
            } else {
                log.info("itemService, ItemsNotFoundException - success");
                throw new ItemsNotFoundException("Items with this model and category are not found");
            }
        } else {
            log.info("itemService, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Items are not found");
        }
    }

    public Item findItemBySerialNumber(String serialNumber) {
        if (itemRepository.findAll().size() != 0) {
            if (itemRepository.existsItemBySerialNumber(serialNumber)) {
                log.info("itemService, findItemBySerialNumber - success");
                return itemRepository.findItemBySerialNumber(serialNumber);
            } else {
                log.info("itemService, ItemNotFoundException - success");
                throw new ItemNotFoundException("Item is not found");
            }
        } else {
            log.info("itemService, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Item are not found");
        }
    }

    public List<Item> findAllItems() {
        if (itemRepository.findAll().size() != 0) {
            log.info("itemService, findAllItem - success");
            return itemRepository.findAll();
        } else {
            log.info("itemService, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Items are not found");
        }
    }

    public Item findItemById(long id) {
        if (itemRepository.findAll().size() != 0) {
            if (itemRepository.existsItemById(id)) {
                log.info("ItemService, findItemById - success");
                return itemRepository.findItemById(id);
            } else {
                log.info("ItemService, ItemNotFoundException - success");
                throw new ItemNotFoundException("Item is not found");
            }
        } else {
            log.info("ItemService, ItemNotFoundException - success");
            throw new ItemsNotFoundException("Items are not found");
        }
    }

    public void updateItemStatusById(long id) {
        if (itemRepository.findAll().size() != 0) {
            if (itemRepository.existsItemById(id)) {
                Item item = itemRepository.findItemById(id);
                item.setItemStatus(ItemStatus.SOLD);
                itemRepository.save(item);
                log.info("ItemService, updateItemStatusById - success");
            } else {
                log.info("ItemService, ItemNotFoundException - success");
                throw new ItemNotFoundException("Item is not found");
            }
        } else {
            log.info("ItemService, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Items are not found");
        }
    }

    public int findNumbersItemByModelAndItemStatusAndCategoryName(String model, ItemStatus ItemStatus, String name) {
        if (itemRepository.findAll().size() != 0) {
            if (itemRepository.existsItemsByModelAndItemStatusAndCategoryName(model, ItemStatus, name)) {
                log.info("ItemService, findNumbersItemByModelAndItemStatusAndCategoryName - success");
                return itemRepository.findItemsByModelAndItemStatusAndCategoryName(model, ItemStatus, name).size();
            } else {
                log.info("ItemService, ItemsNotFoundException - success");
                throw new ItemsNotFoundException("Items are not found");
            }
        } else {
            log.info("ItemService, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Items are not found");
        }
    }

    public void updateItem(Order order, long id) {
        Item item = itemRepository.findItemById(id);
        item.setOrder(order);
        itemRepository.save(item);
        log.info("ItemService, updateItem - success");
    }

    public List<Item> findItemByModelAndItemStatusAndCategoryName(String model, ItemStatus ItemStatus, String name) {
        if (itemRepository.findAll().size() != 0) {
            if(itemRepository.existsItemsByModelAndItemStatusAndCategoryName(model, ItemStatus, name)) {
                log.info("ItemService, findItemByModelAndItemStatusAndCategoryName - success");
                return itemRepository.findItemsByModelAndItemStatusAndCategoryName(model, ItemStatus, name);
            } else {
                log.info("ItemService, ItemsNotFoundException - success");
                throw new ItemsNotFoundException("Items are not found");
            }
        } else {
            log.info("ItemService, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Items are not found");
        }
    }

    public List<Item> findItemsByStatus(ItemStatus ItemStatus) {
        if (itemRepository.findAll().size() != 0) {
            if(itemRepository.existsItemsByItemStatus(ItemStatus)) {
                log.info("ItemService, findItemsByStatus - success");
                return itemRepository.findItemsByItemStatus(ItemStatus);
            } else {
                log.info("ItemService, ItemsNotFoundException - success");
                throw new ItemsNotFoundException("Items are not found");
            }
        } else {
            log.info("ItemService, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Items are not found");
        }
    }
}
