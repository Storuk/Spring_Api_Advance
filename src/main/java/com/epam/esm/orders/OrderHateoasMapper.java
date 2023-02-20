package com.epam.esm.orders;

import com.epam.esm.giftcertificate.GiftCertificate;
import com.epam.esm.giftcertificate.GiftCertificateController;
import com.epam.esm.giftcertificate.GiftCertificateHateoasMapper;
import com.epam.esm.tag.TagController;
import com.epam.esm.user.UserController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Vlad Storoshchuk
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderHateoasMapper {
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    private final GiftCertificateHateoasMapper giftCertificateHateoasMapper;

    /**
     * A component method for adding links to created Order
     *
     * @param order created Order
     * @return CollectionModel of Order with links
     */
    public CollectionModel<Order> createOrderHateoasMapper(Order order) {
        CollectionModel<Order> createdOrder = CollectionModel.of(List.of(order));
        if (!order.getUser().hasLinks()) {
            order.getUser()
                    .add(linkTo(methodOn(UserController.class)
                            .getUserById((order.getUser().getId())))
                            .withRel(() -> "get user"))
                    .add(linkTo(methodOn(OrderController.class)
                            .getOrdersByUserId(order.getUser().getId(), 0, 10))
                            .withRel(() -> "get orders by user id"));
        }

        if (order.getGiftCertificate().getLinks().isEmpty()) {
            giftCertificateHateoasMapper.giftCertificateDefaultLinks(order.getGiftCertificate());
        }

        if (order.getGiftCertificate().getTags() != null) {
            giftCertificateHateoasMapper.tagsDefaultLinks(order.getGiftCertificate());
        }

        createdOrder
                .add(linkTo(methodOn(OrderController.class)
                        .getAllOrders(0, 10))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getAllCertificates(0, 10))
                        .withRel(() -> "get all gift-certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByPartOfDescription("Description", 0, 10))
                        .withRel(() -> "get all gift certificates by part of description"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByName("DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByNameByDate("DESC", "DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name and by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"))
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
                        .withRel(() -> "get gift certificates by tags names"));
        return createdOrder;
    }

    /**
     * A component method for adding links to user Orders
     *
     * @param pagedOrders user Orders
     * @return PagedModel of Order with links
     */
    public PagedModel<Order> getUserOrdersHateoasMapper(Page<Order> pagedOrders) {
        PagedModel<Order> orders = pagedResourcesAssembler
                .toModel(pagedOrders, order -> {

                    if (!order.getUser().hasLinks()) {
                        order.getUser()
                                .add(linkTo(methodOn(UserController.class)
                                        .getUserById((order.getUser().getId())))
                                        .withRel(() -> "get user"))
                                .add(linkTo(methodOn(OrderController.class)
                                        .createOrder(order.getUser().getId(), 0))
                                        .withRel(() -> "create order"));
                    }

                    if (order.getGiftCertificate().getLinks().isEmpty()) {
                        giftCertificateHateoasMapper.giftCertificateDefaultLinks(order.getGiftCertificate());
                    }

                    if (order.getGiftCertificate().getTags() != null) {
                        giftCertificateHateoasMapper.tagsDefaultLinks(order.getGiftCertificate());
                    }
                    return order;
                });

        orders.add(linkTo(methodOn(OrderController.class)
                .getAllOrders(0, 10))
                .withRel(() -> "get all orders"));

        defaultLinksForGetAllOrders(orders);
        return orders;
    }

    /**
     * A component method for adding links to all Orders
     *
     * @param pagedOrders user Orders
     * @return PagedModel of Order with links
     */
    public PagedModel<Order> getAllOrdersHateoasMapper(Page<Order> pagedOrders) {
        PagedModel<Order> orders = pagedResourcesAssembler
                .toModel(pagedOrders, order -> {
                    if (!order.getUser().hasLinks()) {
                        order.getUser()
                                .add(linkTo(methodOn(UserController.class)
                                        .getUserById((order.getUser().getId())))
                                        .withRel(() -> "get user"))
                                .add(linkTo(methodOn(OrderController.class)
                                        .createOrder(order.getUser().getId(), 0))
                                        .withRel(() -> "create order"))
                                .add(linkTo(methodOn(OrderController.class)
                                        .getOrdersByUserId(order.getUser().getId(), 0, 10))
                                        .withRel(() -> "get orders by user id"));
                    }

                    if (order.getGiftCertificate().getLinks().isEmpty()) {
                        giftCertificateHateoasMapper.giftCertificateDefaultLinks(order.getGiftCertificate());
                    }

                    if (order.getGiftCertificate().getTags() != null) {
                        giftCertificateHateoasMapper.tagsDefaultLinks(order.getGiftCertificate());
                    }
                    return order;
                });
        defaultLinksForGetAllOrders(orders);
        return orders;
    }

    /**
     * A component method for adding default links to Page of Orders
     *
     * @param orders Page of Orders
     */
    private void defaultLinksForGetAllOrders(PagedModel<Order> orders) {
        orders
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getAllCertificates(0, 10))
                        .withRel(() -> "get all gift certificates"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesByPartOfDescription("Description", 0, 10))
                        .withRel(() -> "get all gift certificates by part of description"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByName("DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .getGiftCertificatesSortedByNameByDate("DESC", "DESC", 0, 10))
                        .withRel(() -> "get all gift certificates sorted by name and by date"))
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .createCertificate(new GiftCertificate()))
                        .withRel(() -> "create GiftCertificate"))
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
                        .withRel(() -> "get gift certificates by tags names"));
    }
}