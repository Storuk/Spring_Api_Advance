package com.epam.esm.user;

import com.epam.esm.orders.OrderController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Vlad Storoshchuk
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserHateoasMapper {
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;

    /**
     * A component method for adding links to all Users
     *
     * @param pagedUsers Page of Users
     * @return PagedModel of Users with links
     */
    public PagedModel<User> getAllUsersHateoas(Page<User> pagedUsers) {
        PagedModel<User> users = pagedResourcesAssembler
                .toModel(pagedUsers, user -> {
                    user.add(linkTo(methodOn(UserController.class)
                            .getUserById(new User()))
                            .withRel(() -> "get user by id"));
                    user.add(linkTo(methodOn(OrderController.class)
                            .getOrdersByUserId(new User(), 0, 10))
                            .withRel(() -> "get user orders"));
                    user.add(linkTo(methodOn(OrderController.class)
                            .createOrder(new User(), 0))
                            .withRel(() -> "create order"));
                    return user;
                });
        users
                .add(linkTo(methodOn(OrderController.class)
                        .getAllOrders(0, 10))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(UserController.class)
                        .getUserByIdAdminTool(1))
                        .withRel(() -> "get user by id (Admin Tool)"))
                .add(linkTo(methodOn(OrderController.class)
                        .getOrdersByUserIdAdminTool(1, 0, 10))
                        .withRel(() -> "get user orders (Admin Tool)"));

        return users;
    }

    /**
     * A component method for adding links to User
     *
     * @param user User
     * @return CollectionModel of User with links
     */
    public CollectionModel<User> getUserByIdHateoas(User user) {
        user.add(linkTo(methodOn(OrderController.class)
                .getOrdersByUserId(new User(), 0, 10))
                .withRel(() -> "get user orders"));
        user.add(linkTo(methodOn(OrderController.class)
                .createOrder(new User(), 0))
                .withRel(() -> "create order"));
        return CollectionModel.of(List.of(user))
                .add(linkTo(methodOn(OrderController.class)
                        .getAllOrders(0, 10))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(UserController.class)
                        .getAllUsers(0, 10))
                        .withRel(() -> "get all users"))
                .add(linkTo(methodOn(UserController.class)
                        .getUserByIdAdminTool(1))
                        .withRel(() -> "get user by id (Admin Tool)"))
                .add(linkTo(methodOn(OrderController.class)
                        .getOrdersByUserIdAdminTool(1, 0, 10))
                        .withRel(() -> "get user orders (Admin Tool)"));
    }

    public CollectionModel<User> getUserByIdAdminToolHateoas(User user) {
        user.add(linkTo(methodOn(OrderController.class)
                .getOrdersByUserId(new User(), 0, 10))
                .withRel(() -> "get user orders"));
        user.add(linkTo(methodOn(OrderController.class)
                .createOrder(new User(), 0))
                .withRel(() -> "create order"));
        return CollectionModel.of(List.of(user))
                .add(linkTo(methodOn(OrderController.class)
                        .getAllOrders(0, 10))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(UserController.class)
                        .getAllUsers(0, 10))
                        .withRel(() -> "get all users"))
                .add(linkTo(methodOn(OrderController.class)
                        .getOrdersByUserIdAdminTool(1, 0, 10))
                        .withRel(() -> "get user orders (Admin Tool)"));
    }
}