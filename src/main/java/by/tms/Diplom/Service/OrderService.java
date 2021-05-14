package by.tms.Diplom.Service;

import by.tms.Diplom.Entity.Item;
import by.tms.Diplom.Entity.ItemStatus;
import by.tms.Diplom.Entity.Order;
import by.tms.Diplom.Entity.OrderStatus;
import by.tms.Diplom.Repository.ItemRepository;
import by.tms.Diplom.Repository.OrderRepository;
import by.tms.Diplom.Service.Exception.ItemsNotFoundException;
import by.tms.Diplom.Service.Exception.NotEnoughMoneyException;
import by.tms.Diplom.Service.Exception.OrderNotFoundException;
import by.tms.Diplom.Service.Exception.OrdersNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    public void addOrder(Order newOrder) {
        orderRepository.save(newOrder);
        log.info("orderService, addOrder - success");
    }

    public Order findOrderById(long id) {
        if (orderRepository.findAll().size() != 0) {
            if(orderRepository.existsOrderById(id)) {
                log.info("orderService, findOrderById - success");
                return orderRepository.findOrderById(id);
            } else {
                log.info("orderService, OrderNotFoundException - success");
                throw new OrderNotFoundException("Order or user is not found");
            }
        } else {
            log.info("orderService, OrdersNotFoundException - success");
            throw new OrdersNotFoundException("Orders are not found");
        }
    }

    public String orderAmountByOrderId(long id) {
        if(orderRepository.existsOrderById(id)) {
            double price = 0;
            List<Item> items;
            items = orderRepository.findOrderById(id).getItemList();
            for (int i = 0; i < items.size(); i++) {
                price+= Double.parseDouble(items.get(i).getPrice());
            }
            log.info("orderService, orderAmountByOrderId - success");
            return Double.toString(price);
        } else {
            log.info("orderService, OrderNotFoundException - success");
            throw new OrderNotFoundException("Order or user is not found");
        }
    }

    public List<Order> findAllOrdersByUserLogin(String login) {
        if (orderRepository.findOrdersByUserLogin(login).size() != 0) {
            log.info("orderService, findAllOrdersByUserLogin - success");
            return orderRepository.findOrdersByUserLogin(login);
        } else {
            log.info("orderService, OrdersNotFoundException - success");
            throw new OrdersNotFoundException("Orders by this login are not found");
        }
    }

    public List<Order> findAllOrdersByOrderStatus(OrderStatus orderStatus) {
        if (orderRepository.findOrdersByOrderStatus(orderStatus).size() != 0) {
            log.info("orderService, findAllOrdersByOrderStatus - success");
            return orderRepository.findOrdersByOrderStatus(orderStatus);
        } else {
            log.info("orderService, OrdersNotFoundException - success");
            throw new OrdersNotFoundException("Orders by this status are not found");
        }
    }

    public List<Order> findAllOrders() {
        if (orderRepository.findAll().size() != 0) {
            log.info("orderService, findAllOrders - success");
            return orderRepository.findAll();
        } else {
            log.info("orderService, OrdersNotFoundException - success");
            throw new OrdersNotFoundException("Orders are not found");
        }
    }

    public void removeItemsFromOrderByIdAndUserLogin(long orderId, Item item, String login) {
        if (orderRepository.findAll().size() != 0) {
            if(orderRepository.existsOrderById(orderId) && orderRepository.findOrderById(orderId).getUser().getLogin().equals(login)) {
                Order order = orderRepository.findOrderById(orderId);
                for (int i = 0; i < order.getItemList().size(); i++) {
                    Item item1 = order.getItemList().get(i);
                    if(order.getItemList().contains(item)) {
                        item1.setOrder(null);
                        itemRepository.save(item1);
                        order.getItemList().remove(item1);
                        orderRepository.save(order);
                    }
                }
                log.info("orderService, removeItemFromOrderByIdAndUserLogin - success");
            } else {
                log.info("orderService, OrderNotFoundException - success");
                throw new OrderNotFoundException("Order is not found");
            }
        } else {
            log.info("orderService, OrdersNotFoundException - success");
            throw new OrdersNotFoundException("Orders are not found");
        }
    }

    public void purchaseItemByUserIdAndOrderId(long userId, long orderId) {
        if (orderRepository.findAll().size() != 0) {
            if (orderRepository.existsOrderByIdAndUserId(orderId, userId)) {
                double tempWallet = 0;
                double tempUserWallet = Double.parseDouble(orderRepository.findOrderById(orderId).getUser().getWallet());
                for (int i = 0; i < orderRepository.findOrderById(orderId).getItemList().size(); i++) {
                    tempWallet += Double.parseDouble(orderRepository.findOrderById(orderId).getItemList().get(i).getPrice());
                }
                if (tempUserWallet >= tempWallet) {
                    double newWallet = tempUserWallet - tempWallet;
                    orderRepository.findOrderById(orderId).getUser().setWallet(Double.toString(newWallet));
                    Order order = orderRepository.findOrderById(orderId);
                    order.setOrderStatus(OrderStatus.DELIVERED);
                    LocalDate date = LocalDate.now();
                    order.setOrderDeliveredDate(date.toString());
                    orderRepository.save(order);
                    log.info("orderService, purchaseItemByUserIdAndOrderId - success");
                } else {
                    Order order = orderRepository.findOrderById(orderId);
                    order.setOrderStatus(OrderStatus.PROCESSED);
                    orderRepository.save(order);
                    log.info("orderService, NotEnoughMoneyException - success");
                    throw new NotEnoughMoneyException("Up your wallet");
                }
            } else {
                log.info("orderService, OrderNotFoundException - success");
                throw new OrderNotFoundException("Order or user is not found");
            }
        } else {
            log.info("orderService, OrdersNotFoundException - success");
            throw new OrdersNotFoundException("Orders are not found");
        }
    }

    public void updateOrderByIdAndItemList(long id, List<Item> items) {
        if (orderRepository.findAll().size() != 0) {
            if (orderRepository.existsOrderById(id)) {
                Order order = orderRepository.findOrderById(id);
                order.setItemList(items);
                orderRepository.save(order);
                log.info("orderService, updateOrderByIdAndItemList - success");
            } else {
                log.info("orderService, OrderNotFoundException - success");
                throw new OrderNotFoundException("Order is not found");
            }
        } else {
            log.info("orderService, OrdersNotFoundException - success");
            throw new OrdersNotFoundException("Orders are not found");
        }
    }

    public List<Order> findOrdersByLogin(String login) {
        log.info("orderService, findOrdersByLogin - success");
        return orderRepository.findOrdersByUserLogin(login);
    }

    public void checkOrderForItemStatus(Order order) {
        if (orderRepository.findAll().size() != 0) {
            if(orderRepository.existsOrderById(order.getId())) {
                List<Item> items = new ArrayList<>();
                for (Item item : order.getItemList()) {
                    if(item.getItemStatus().equals(ItemStatus.ACTIVE)) {
                        items.add(item);
                    }
                }
                if(items.size() != order.getItemList().size()) {
                    updateOrderByIdAndItemList(order.getId(), items);
                    log.info("orderService, ItemsNotFoundException - success");
                    throw new ItemsNotFoundException("Your order is update, because some Items are sold");
                }
            } else {
                log.info("orderService, OrderNotFoundException - success");
                throw new OrderNotFoundException("Order is not found");
            }
        } else {
            log.info("orderService, OrdersNotFoundException - success");
            throw new OrdersNotFoundException("Orders are not found");
        }
    }
    @Transactional
    public void deleteOrderById(long id) {
        orderRepository.deleteOrderById(id);
        log.info("orderService, deleteOrderById - success");
    }
}