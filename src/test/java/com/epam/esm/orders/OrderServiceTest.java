package com.epam.esm.orders;

import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.giftcertificate.GiftCertificateService;
import com.epam.esm.user.User;
import com.epam.esm.user.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    GiftCertificateService giftCertificateServiceMock;
    @Mock
    UserService userServiceMock;
    @Mock
    OrderRepo orderRepoMock;
    @InjectMocks
    OrderService orderServiceMock;
    @Test
    void createOrderTest() {
        GiftCertificate giftCertificate = GiftCertificate.builder().price(1).build();
        Order order = Order.builder().cost(1)
                .giftCertificate(giftCertificate).user(new User()).build();
        when(giftCertificateServiceMock.getGiftCertificateById(1L)).thenReturn(giftCertificate);
        when(userServiceMock.getUserById(1L)).thenReturn(new User());
        when(orderRepoMock.save(order)).thenReturn(order);
        assertEquals(order,orderServiceMock.createOrder(1L, 1L));
    }

    @Test
    void getAllOrdersTest() {
        Page<Order> orders = new PageImpl<>(List.of(new Order()));
        when(orderRepoMock.findAll(PageRequest.of(0, 3))).thenReturn(orders);
        assertEquals(orders,orderServiceMock.getAllOrders(0, 3));
    }

    @Test
    void getOrdersByUserIdTestTrue_WhenListIsNotEmpty() {
        Page<Order> allGiftCertificates = new PageImpl<>(List.of(new Order()));
        when(orderRepoMock.getOrdersByUserId(1L,PageRequest.of(0,3))).thenReturn(allGiftCertificates);
        assertEquals(allGiftCertificates,orderServiceMock.getOrdersByUserId(1L,0,3));
    }

    @Test
    void getOrdersByUserIdTest_ItemNotFoundException_WhenListIsEmpty() {
        long user_id = 1L;
        Page<Order> allGiftCertificates = new PageImpl<>(List.of());
        when(orderRepoMock.getOrdersByUserId(user_id,PageRequest.of(0,3))).thenReturn(allGiftCertificates);
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> orderServiceMock.getOrdersByUserId(user_id,0,3));
        assertEquals("No orders where user_id = " + user_id, exception.getMessage());
    }

}