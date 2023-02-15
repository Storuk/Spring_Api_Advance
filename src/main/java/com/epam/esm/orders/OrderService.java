package com.epam.esm.orders;

import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.giftcertificate.GiftCertificateService;
import com.epam.esm.tag.TagRepo;
import com.epam.esm.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vlad Storoshchuk
 * */
@Service
@EnableTransactionManagement
public class OrderService {
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final OrderRepo orderRepo;
    public OrderService(UserService userService, GiftCertificateService giftCertificateService, OrderRepo orderRepo) {
        this.userService = userService;
        this.giftCertificateService = giftCertificateService;
        this.orderRepo = orderRepo;
    }

    /**
     * A service method for creating Order
     * @param user_id User id for creating order
     * @param giftCertificateId GiftCertificate id for creating order
     * @return created Order
     * @see TagRepo#save(Object) for saving Tag
     * */
    @Transactional
    public Order createOrder(long user_id, long giftCertificateId) {
        GiftCertificate orderedGiftCertificate = giftCertificateService.getGiftCertificateById(giftCertificateId);
        Order newOrder = Order.builder().giftCertificate(orderedGiftCertificate)
                .cost(orderedGiftCertificate.getPrice())
                .user(userService.getUserById(user_id)).build();
        return orderRepo.save(newOrder);
    }

    /**
     * A service method for getting all orders
     * @param page number of page (min value 0)
     * @param size count of Tags (min value 1)
     * @return Page of Orders
     * @see OrderRepo#findAll() for getting All Orders
     * */
    public Page<Order> getAllOrders(int page, int size) {
        return orderRepo.findAll(PageRequest.of(page, size));
    }

    /**
     * A service method for getting User orders by user id
     * @param user_id User id
     * @param page number of page (min value 0)
     * @param size count of Tags (min value 1)
     * @return Page of Orders
     * @see OrderRepo#getOrdersByUserId(long, Pageable) for getting All Orders
     * */
    public Page<Order> getOrdersByUserId(long user_id, int page, int size) {
        Page<Order> order = orderRepo.getOrdersByUserId(user_id, PageRequest.of(page, size));
        if(!order.isEmpty()){
            return order;
        }
        throw new ItemNotFoundException("No orders where user_id = " + user_id);
    }
}