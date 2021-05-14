package by.tms.Diplom.Controller;

import by.tms.Diplom.Entity.Item;
import by.tms.Diplom.Entity.Order;
import by.tms.Diplom.Entity.OrderStatus;
import by.tms.Diplom.Entity.UserRole;
import by.tms.Diplom.Service.Exception.OrderNotFoundException;
import by.tms.Diplom.Service.ItemService;
import by.tms.Diplom.Service.OrderService;
import by.tms.Diplom.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(path = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ModelAndView orderGet(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("orders", orderService.findAllOrdersByUserLogin(authentication.getName()));
        modelAndView.setViewName("orders");
        log.info("orderController, orderGet - success");
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getOrderView(@PathVariable("id") long id, ModelAndView modelAndView) {
        int marker;
        Order order = orderService.findOrderById(id);
        if(order.getItemList().isEmpty()) {
            orderService.deleteOrderById(id);
            modelAndView.setViewName("redirect:/");
            log.info("orderController, getOrderView(Null) - success");
        } else {
            modelAndView.addObject("Items", order.getItemList());
            modelAndView.addObject("order", order);
            modelAndView.setViewName("order");
            modelAndView.addObject("price", orderService.orderAmountByOrderId(id));
            if (orderService.findOrderById(id).getOrderStatus().equals(OrderStatus.DELIVERED)) {
                marker = 1;
            } else {
                marker = 0;
            }
            modelAndView.addObject("marker", marker);
            log.info("orderController, getOrderView(NotNull) - success");
        }
        return modelAndView;
    }

    @PostMapping("/{id}")
    public ModelAndView postOrderPay(@PathVariable("id") long id, @ModelAttribute("price") String price, ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Order order = orderService.findOrderById(id);
        orderService.checkOrderForItemStatus(order);
        orderService.purchaseItemByUserIdAndOrderId(userService.findUserByLogin(authentication.getName()).getId(), id);
        for (Item item : order.getItemList()) {
            itemService.updateItemStatusById(item.getId());
        }
        modelAndView.setViewName("redirect:/order");
        log.info("orderController, postOrderPay - success");
        return modelAndView;
    }

    @PostMapping("/remove/{id}")
    public ModelAndView postOrderRemove(@PathVariable("id") long id, long itemId, ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!orderService.findOrderById(id).getOrderStatus().equals(OrderStatus.DELIVERED)) {
            orderService.removeItemsFromOrderByIdAndUserLogin(id, itemService.findItemById(itemId), authentication.getName());
            modelAndView.setViewName("redirect:/order/" + id);
            log.info("orderController, postOrderRemove - success");
        } else {
            log.info("orderController, OrderNotFoundException - success");
            throw new OrderNotFoundException("You can't remove an item from an order when it's purchased");
        }
        return modelAndView;
    }

    @GetMapping("/all")
    public ModelAndView getOrdersAll(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN) || userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.addObject("allOrders", orderService.findAllOrders());
            modelAndView.setViewName("allOrders");
            log.info("orderController, getOrdersAll(ADMIN or MANAGER) - success");
        } else {
            log.info("orderController, getOrdersAll(USER) - success");
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    @GetMapping("/allOrdersByStatus")
    public ModelAndView getOrdersByStatus(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.ADMIN) || userService.findUserByLogin(authentication.getName()).getUserRole().equals(UserRole.MANAGER)) {
            modelAndView.setViewName("allOrdersByStatus");
            log.info("orderController, getOrdersByStatus(ADMIN or MANAGER) - success");
        } else {
            modelAndView.setViewName("redirect:/");
            log.info("orderController, getOrdersByStatus(USER) - success");
        }
        return modelAndView;
    }

    @PostMapping("/allOrdersByStatus")
    public ModelAndView postOrdersByStatus(OrderStatus orderStatus, ModelAndView modelAndView) {
        modelAndView.addObject("allOrders", orderService.findAllOrdersByOrderStatus(orderStatus));
        modelAndView.setViewName("ordersByStatus");
        log.info("orderController, postOrdersByStatus - success");
        return modelAndView;
    }
}