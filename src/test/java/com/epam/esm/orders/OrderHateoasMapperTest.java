package com.epam.esm.orders;

import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.giftcertificate.GiftCertificateController;
import com.epam.esm.giftcertificate.GiftCertificateDTO;
import com.epam.esm.tag.TagController;
import com.epam.esm.user.User;
import com.epam.esm.user.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class OrderHateoasMapperTest {
    @InjectMocks
    private OrderHateoasMapper orderHateoasMapper;
    @Mock
    private PagedResourcesAssembler<Order> orderPagedResourcesAssembler;
    @Captor
    ArgumentCaptor<Link> argumentCaptor;

    @Test
    void createOrderHateoasMapperTest() {
        User user = User.builder().id(1L).email("user").build();
        GiftCertificate giftCertificate = GiftCertificate.builder().id(1L)
                .name("certificate").description(null).price(1).duration(1)
                .tags(null).createDate(null).lastUpdateDate(null).build();
        Order order = Order.builder().id(1L).purchaseDate(null).giftCertificate(giftCertificate)
                .user(user).cost(1).build();

        User userForTest = User.builder().id(1L).email("user").build();
        GiftCertificate giftCertificateForTest = GiftCertificate.builder().id(1L)
                .name("certificate").description(null).price(1).duration(1)
                .tags(null).createDate(null).lastUpdateDate(null).build();
        Order orderForTest = Order.builder().id(1L).purchaseDate(null).giftCertificate(giftCertificateForTest)
                .user(userForTest).cost(1).build();

        CollectionModel<Order> createdOrder = CollectionModel.of(List.of(order));
        order.getUser()
                .add(linkTo(methodOn(UserController.class)
                        .getUserById(new User()))
                        .withRel(() -> "get user"))
                .add(linkTo(methodOn(OrderController.class)
                        .getOrdersByUserId(new User(), 0, 10))
                        .withRel(() -> "get orders by user id"));

        order.getGiftCertificate()
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificateById(giftCertificate.getId()))
                        .withRel(() -> "get gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .deleteGiftCertificate(giftCertificate.getId()))
                        .withRel(() -> "delete gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateGiftCertificate(giftCertificate.getId(), new GiftCertificateDTO()))
                        .withRel(() -> "update gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updatePrice(giftCertificate.getId(), 0))
                        .withRel(() -> "update price in gift certificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .updateDuration(giftCertificate.getId(), 0))
                        .withRel(() -> "update duration in gift certificate"));

        createdOrder
                .add(linkTo(methodOn(OrderController.class)
                        .getAllOrders(0, 10))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificateDTO()))
                        .withRel(() -> "create GiftCertificate"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getAllCertificates(0, 10))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByName("DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByNameByDate("DESC", "DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name and by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByPartOfDescription("Description", 0, 10))
                        .withRel(() -> "get all gift certificates by part of description"))
                .add(linkTo(methodOn(TagController.class)
                        .getAllTags(0, 10))
                        .withRel(() -> "get all tags"))
                .add(linkTo(methodOn(TagController.class)
                        .getTheMostlyUsedTagInUserOrders())
                        .withRel(() -> "get the mostly used tag from user with highest sum of orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTagName("tagName", 0, 10))
                        .withRel(() -> "get gift certificates by tag name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByTags(Set.of(), 0, 10))
                        .withRel(() -> "get gift certificates by tags names"))
                .add(linkTo(methodOn(UserController.class)
                        .getUserByIdAdminTool(1))
                        .withRel(() -> "get user by id (Admin Tool)"))
                .add(linkTo(methodOn(OrderController.class)
                        .getOrdersByUserIdAdminTool(1, 0, 10))
                        .withRel(() -> "get user orders (Admin Tool)"));

        assertEquals(createdOrder.getContent().stream().toList(),
                orderHateoasMapper.createOrderHateoasMapper(orderForTest).getContent().stream().toList());
    }

    @Test
    void getUserOrdersHateoasMapperTest() {
        Page<Order> orders = new PageImpl<>(List.of(new Order()));
        PagedModel<Order> orderPagedModel = mock(PagedModel.class);

        when(orderPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(orderPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(orders), ArgumentMatchers.<RepresentationModelAssembler<Order, Order>>any())).thenReturn(orderPagedModel);

        PagedModel<Order> result = orderHateoasMapper.getUserOrdersHateoasMapper(orders);

        assertEquals(result, orderPagedModel);
        verify(result, times(12)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "get all orders");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get user orders (Admin Tool)");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all gift certificates");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get all gift certificates by part of description");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get all gift certificates sorted by name");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "get all gift certificates sorted by name and by date");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(9).getRel().value(), "get gift certificates by tag name");
        assertEquals(argumentCaptor.getAllValues().get(10).getRel().value(), "get gift certificates by tags names");
        assertEquals(argumentCaptor.getAllValues().get(11).getRel().value(), "get user by id (Admin Tool)");
    }

    @Test
    void getUserOrdersAdminToolHateoasMapperTest() {
        Page<Order> orders = new PageImpl<>(List.of(new Order()));
        PagedModel<Order> orderPagedModel = mock(PagedModel.class);

        when(orderPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(orderPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(orders), ArgumentMatchers.<RepresentationModelAssembler<Order, Order>>any())).thenReturn(orderPagedModel);

        PagedModel<Order> result = orderHateoasMapper.getUserOrdersAdminToolHateoasMapper(orders);

        assertEquals(result, orderPagedModel);
        verify(result, times(11)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "get all orders");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all gift certificates");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all gift certificates by part of description");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get all gift certificates sorted by name");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get all gift certificates sorted by name and by date");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get gift certificates by tag name");
        assertEquals(argumentCaptor.getAllValues().get(9).getRel().value(), "get gift certificates by tags names");
        assertEquals(argumentCaptor.getAllValues().get(10).getRel().value(), "get user by id (Admin Tool)");
    }

    @Test
    void getAllOrdersHateoasMapperTest() {
        Page<Order> orders = new PageImpl<>(List.of(new Order()));
        PagedModel<Order> orderPagedModel = mock(PagedModel.class);

        when(orderPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(orderPagedModel);
        when(orderPagedResourcesAssembler.toModel(eq(orders),
                ArgumentMatchers.<RepresentationModelAssembler<Order, Order>>any())).thenReturn(orderPagedModel);

        PagedModel<Order> result = orderHateoasMapper.getAllOrdersHateoasMapper(orders);

        assertEquals(result, orderPagedModel);
        verify(result, times(11)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "get user orders (Admin Tool)");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get all gift certificates");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get all gift certificates by part of description");
        assertEquals(argumentCaptor.getAllValues().get(3).getRel().value(), "get all gift certificates sorted by name");
        assertEquals(argumentCaptor.getAllValues().get(4).getRel().value(), "get all gift certificates sorted by name and by date");
        assertEquals(argumentCaptor.getAllValues().get(5).getRel().value(), "create GiftCertificate");
        assertEquals(argumentCaptor.getAllValues().get(6).getRel().value(), "get all tags");
        assertEquals(argumentCaptor.getAllValues().get(7).getRel().value(), "get the mostly used tag from user with highest sum of orders");
        assertEquals(argumentCaptor.getAllValues().get(8).getRel().value(), "get gift certificates by tag name");
        assertEquals(argumentCaptor.getAllValues().get(9).getRel().value(), "get gift certificates by tags names");
        assertEquals(argumentCaptor.getAllValues().get(10).getRel().value(), "get user by id (Admin Tool)");
    }
}