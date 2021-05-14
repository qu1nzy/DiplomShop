package by.tms.Diplom.Controller;

import by.tms.Diplom.Entity.Basket;
import by.tms.Diplom.Entity.Item;
import by.tms.Diplom.Entity.Order;
import by.tms.Diplom.Entity.OrderStatus;
import by.tms.Diplom.Service.Exception.ItemsNotFoundException;
import by.tms.Diplom.Service.ItemService;
import by.tms.Diplom.Service.OrderService;
import by.tms.Diplom.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/basket")
@Slf4j
public class BasketController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ModelAndView basketGet(ModelAndView modelAndView) {
        modelAndView.setViewName("basket");
        log.info("basketController, basketGet - success");
        return modelAndView;
    }

    @PostMapping
    public ModelAndView basketPost(ModelAndView modelAndView, HttpSession httpSession) {
        Basket basket = (Basket) httpSession.getAttribute("basket");
        Order order = new Order();
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < basket.getItemList().size(); i++) {
            long id = basket.getItemList().get(i).getId();
            Item item = itemService.findItemById(id);
            if(item.getItemStatus().equals(basket.getItemList().get(i).getItemStatus())) {
                itemList.add(basket.getItemList().get(i));
            }
        }
        if(itemList.size() != basket.getItemList().size()) {
            basket.setItemList(itemList);
            log.info("basketController, ItemsNotFoundException - success");
            throw new ItemsNotFoundException("Some items are sold, please refresh your basket");
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            order.setUser(userService.findUserByLogin(authentication.getName()));
            order.setItemList(basket.getItemList());
            order.setOrderStatus(OrderStatus.NEW);
            LocalDate date = LocalDate.now();
            order.setOrderCreatedDate(date.toString());
            order.setOrderDeliveredDate("-");
            orderService.addOrder(order);
            for (int i = 0; i < basket.getItemList().size(); i++) {
                itemService.updateItem(order, basket.getItemList().get(i).getId());
            }
            basket.getItemList().clear();
            modelAndView.setViewName("redirect:/");
            log.info("basketController, basketPost - success");
        }
        return modelAndView;
    }
}