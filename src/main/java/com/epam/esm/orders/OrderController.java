package com.epam.esm.orders;

import com.epam.esm.user.User;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Class OrderController which contain method related with Order
 *
 * @author Vlad Storoshchuk
 */
@Validated
@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    private final OrderService orderService;
    private final OrderHateoasMapper orderHateoasMapper;

    public OrderController(OrderService orderService, OrderHateoasMapper orderHateoasMapper) {
        this.orderService = orderService;
        this.orderHateoasMapper = orderHateoasMapper;
    }

    /**
     * A controller post method for creating a new order
     * @param giftCertificateId - id of gift certificate for which order is made
     * @see OrderService#createOrder(long, long)
     */
    @PostMapping("/{giftCertificateId}")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal User user,
                                         @PathVariable("giftCertificateId")
                                         @Min(value = 1, message = "GiftCertificateId should be >= 1") long giftCertificateId) {
        Order order = orderService.createOrder(user.getId(), giftCertificateId);
        return new ResponseEntity<>(Map.of("order", orderHateoasMapper.createOrderHateoasMapper(order)), HttpStatus.CREATED);
    }

    /**
     * A controller get method for getting orders by user id
     *
     * @param page    - number of page (min value 0)
     * @param size    - count of tags (min value 1)
     * @see OrderService#getOrdersByUserId(long, int, int)
     */
    @GetMapping("by-user-id")
    public ResponseEntity<?> getOrdersByUserId(@AuthenticationPrincipal User user,
                                               @RequestParam(value = "page", defaultValue = "0")
                                               @Min(value = 0, message = "Page index should be >= 0") int page,
                                               @RequestParam(value = "size", defaultValue = "10")
                                               @Min(value = 1, message = "Size should be should be >= 1") int size) {
        Page<Order> order = orderService.getOrdersByUserId(user.getId(), page, size);
        return ResponseEntity.ok(Map.of("userOrders", orderHateoasMapper.getUserOrdersHateoasMapper(order)));
    }

    /**
     * A controller get method for getting all orders
     *
     * @param page - number of page (min value 0)
     * @param size - count of tags (min value 1)
     * @see OrderService#getAllOrders(int, int)
     */
    @GetMapping()
    public ResponseEntity<?> getAllOrders(@RequestParam(value = "page", defaultValue = "0")
                                          @Min(value = 0, message = "Page index should be >= 0") int page,
                                          @RequestParam(value = "size", defaultValue = "20")
                                          @Min(value = 1, message = "Size should be should be >= 10") int size) {
        Page<Order> allOrders = orderService.getAllOrders(page, size);
        return ResponseEntity.ok(Map.of("orders", orderHateoasMapper.getAllOrdersHateoasMapper(allOrders)));
    }
}