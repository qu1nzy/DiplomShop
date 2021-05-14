package by.tms.Diplom.Controller;

import by.tms.Diplom.Entity.*;
import by.tms.Diplom.Service.*;
import by.tms.Diplom.Service.Exception.ItemsNotFoundException;
import by.tms.Diplom.Service.Exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ImageService imageService;


    @GetMapping("add")
    public ModelAndView addItemGet(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN) || userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.setViewName("itemAdd");
            modelAndView.addObject("item", new ItemAdd());
            log.info("itemController, addItemGet(ADMIN or MANAGER) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("itemController, addItemGet(USER) - success");
        }
        return modelAndView;
    }

    @PostMapping("add")
    public ModelAndView addItemPost(@Valid @ModelAttribute("item") ItemAdd itemAdd, Errors errors, ModelAndView modelAndView) throws IOException {
        int marker;
        if (errors.hasErrors()) {
            modelAndView.setViewName("itemAdd");
            log.info("itemController, addItemPost(Error) - success");
        } else {
            if (categoryService.existCategoryByName(itemAdd.getCategoryName())) {
                Item item = new Item();
                item.setCategory(categoryService.findCategoryByName(itemAdd.getCategoryName()));
                item.setDescription(itemAdd.getDescription());
                item.setCountryManufacture(itemAdd.getCountryManufacture());
                item.setModel(itemAdd.getModel());
                item.setSerialNumber(itemAdd.getSerialNumber());
                item.setManufacturer(itemAdd.getManufacturer());
                item.setPrice(itemAdd.getPrice());
                item.setItemStatus(ItemStatus.ACTIVE);
                MultipartFile multiImage = itemAdd.getImage();
                if(multiImage.isEmpty()) {
                    Image image = new Image();
                    image.setUrl("https://enix.ru/wp-content/uploads/2020/03/no-image-900x.jpg");
                    item.setImage(image);
                } else {
                    Image image = imageService.upload(multiImage, "item", itemAdd.getCategoryName(), itemAdd.getModel());
                    item.setImage(image);
                }
                marker = 1;
                itemService.addItem(item);
                log.info("itemController, addItemGet - success");
            } else {
                marker = 2;
                modelAndView.addObject("message", "The entered category name does not exist");
                log.info("itemController, addItemGet(message) - success");
            }
            modelAndView.addObject("marker", marker);
            modelAndView.setViewName("itemAdd");
        }
        return modelAndView;
    }

    @GetMapping("items/{id}")
    public ModelAndView itemGet(@PathVariable("id") long id, ModelAndView modelAndView) {
        int marker = 0;
        Item item = itemService.findItemById(id);
        modelAndView.addObject("item", item);
        modelAndView.setViewName("item");
        modelAndView.addObject("marker", marker);
        modelAndView.addObject("quantity", itemService.findNumbersItemByModelAndItemStatusAndCategoryName(item.getModel(), ItemStatus.ACTIVE, item.getCategory().getName()));
        log.info("itemController, itemGet - success");
        return modelAndView;
    }

    @PostMapping("/addToBasket")
    public ModelAndView postAddItem(long itemId, ModelAndView modelAndView, HttpSession httpSession) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Order> orders = orderService.findOrdersByLogin(authentication.getName());
        for (Order order : orders) {
            if(order.getOrderStatus().equals(OrderStatus.NEW) || order.getOrderStatus().equals(OrderStatus.PROCESSED)) {
                log.info("itemController, OrderNotFoundException - success");
                throw new OrderNotFoundException("Pay for your previous orders");
            }
        }
        Item item = itemService.findItemById(itemId);
        List<Item> items = itemService.findItemByModelAndItemStatusAndCategoryName(item.getModel(), ItemStatus.ACTIVE, item.getCategory().getName());
        Basket basket = (Basket) httpSession.getAttribute("basket");
        int numbersOfItem = items.size();
        if(basket.getItemList().size() > 0) {
            int numOfBasketModel = 0;
            for (int i = 0; i < basket.getItemList().size(); i++) {
                if(basket.getItemList().get(i).getModel().equals(items.get(0).getModel()) && basket.getItemList().get(i).getCategory().equals(items.get(0).getCategory())) {
                    numOfBasketModel++;
                }
            }
            if (numbersOfItem > numOfBasketModel) {
                basket.addItem(items.get(numOfBasketModel));
                log.info("itemController, postAddItem - success");
            } else {
                log.info("itemController, itemsNotFoundException - success");
                throw new ItemsNotFoundException("Items are not found");
            }
        } else {
            basket.addItem(items.get(0));
            log.info("itemController, postAddItem - success");
        }
        modelAndView.setViewName("redirect:/item/items/" + itemId);
        return modelAndView;
    }

    @GetMapping("/allItems")
    public ModelAndView allItemsGet(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN)) {
            modelAndView.addObject("allItems", itemService.findAllItems());
            modelAndView.setViewName("items");
            log.info("itemController, allItemsGet(ADMIN) - success");
        } else if (userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.addObject("allItems", itemService.findAllItems());
            modelAndView.setViewName("items");
            log.info("itemController, allItemsGet(MANAGER) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("itemController, allItemsGet(USER) - success");
        }
        return modelAndView;
    }

    @GetMapping("/serialNumber")
    public ModelAndView getItemBySerialNumber(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN) || userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.setViewName("itemSerialNumber");
            modelAndView.addObject("serialNumber", new ItemSerialNumber());
            log.info("itemController, getItemBySerialNumber(ADMIN or MANAGER) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("itemController, getItemBySerialNumber(USER) - success");
        }
        return modelAndView;
    }

    @PostMapping("/serialNumber")
    public ModelAndView postItemBySerialNumber(@Valid @ModelAttribute("serialNumber") ItemSerialNumber itemSerialNumber,Errors errors, ModelAndView modelAndView) {
        if (errors.hasErrors()) {
            modelAndView.setViewName("itemSerialNumber");
            log.info("itemController, postItemBySerialNumber(Error) - success");
        } else {
            Item item = itemService.findItemBySerialNumber(itemSerialNumber.getSerialNumber());
            int marker = 1;
            modelAndView.addObject("item", item);
            modelAndView.addObject("marker", marker);
            modelAndView.setViewName("item");
            log.info("itemController, postItemBySerialNumber - success");
        }
        return modelAndView;
    }

    @GetMapping("/status")
    public ModelAndView getItemsByStatus(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN) || userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.setViewName("itemStatus");
            log.info("itemController, getItemsByStatus(ADMIN or MANAGER) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("itemController, getItemsByStatus(USER) - success");
        }
        return modelAndView;
    }

    @PostMapping("/status")
    public ModelAndView postItemsByStatus(ItemStatus itemStatus, ModelAndView modelAndView) {
        List<Item> items = itemService.findItemsByStatus(itemStatus);
        modelAndView.addObject("items", items);
        modelAndView.setViewName("itemsByStatus");
        log.info("itemController, postItemsByStatus - success");
        return modelAndView;
    }
}
