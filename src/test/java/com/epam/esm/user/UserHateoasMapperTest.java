package com.epam.esm.user;

import com.epam.esm.orders.OrderController;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserHateoasMapperTest {
    @Mock
    private PagedResourcesAssembler<User> userPagedResourcesAssembler;
    @Captor
    ArgumentCaptor<Link> argumentCaptor;
    @InjectMocks
    private UserHateoasMapper userHateoasMapper;

    @Test
    void getAllUsersHateoas() {
        Page<User> users = new PageImpl<>(List.of(new User()));
        PagedModel<User> userPagedModel = mock(PagedModel.class);

        when(userPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(userPagedModel);
        when(userPagedResourcesAssembler.toModel(eq(users), ArgumentMatchers.<RepresentationModelAssembler<User, User>>any())).thenReturn(userPagedModel);
        PagedModel<User> result = userHateoasMapper.getAllUsersHateoas(users);

        assertEquals(userPagedModel, result);
        verify(result, times(1)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(),"get all orders");
    }

    @Test
    void getUserByIdHateoas() {
        User user = User.builder().id(1L).login("login").build();
        User userForTest = User.builder().id(1L).login("login").build();

        CollectionModel<User> collectionModelUser = CollectionModel.of(List.of(user));
        user.add(linkTo(methodOn(OrderController.class)
                .getOrdersByUserId((user.getId()),0,10))
                .withRel(() -> "get user orders"));
        user.add(linkTo(methodOn(OrderController.class)
                .createOrder(user.getId(), 0))
                .withRel(() -> "create order"));

        collectionModelUser
                .add(linkTo(methodOn(OrderController.class)
                        .getAllOrders(0, 10))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(UserController.class)
                        .getAllUsers(0, 10))
                        .withRel(() -> "get all users"));

        assertEquals(collectionModelUser.getContent().stream().toList(),
                userHateoasMapper.getUserByIdHateoas(userForTest).getContent().stream().toList());

    }
}