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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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
    void getAllUsersHateoasTest() {
        Page<User> users = new PageImpl<>(List.of(new User()));
        PagedModel<User> userPagedModel = mock(PagedModel.class);

        when(userPagedModel.add(ArgumentMatchers.<Link>any())).thenReturn(userPagedModel);
        when(userPagedResourcesAssembler.toModel(eq(users), ArgumentMatchers.<RepresentationModelAssembler<User, User>>any())).thenReturn(userPagedModel);
        PagedModel<User> result = userHateoasMapper.getAllUsersHateoas(users);

        assertEquals(userPagedModel, result);
        verify(result, times(3)).add(argumentCaptor.capture());
        assertEquals(argumentCaptor.getAllValues().get(0).getRel().value(), "get all orders");
        assertEquals(argumentCaptor.getAllValues().get(1).getRel().value(), "get user by id (Admin Tool)");
        assertEquals(argumentCaptor.getAllValues().get(2).getRel().value(), "get user orders (Admin Tool)");
    }

    @Test
    void getUserByIdHateoasTest() {
        User user = User.builder().id(1L).email("email").build();
        User userForTest = User.builder().id(1L).email("email").build();

        CollectionModel<User> collectionModelUser = CollectionModel.of(List.of(user));
        user.add(linkTo(methodOn(OrderController.class)
                .getOrdersByUserId(new User(), 0, 10))
                .withRel(() -> "get user orders"));
        user.add(linkTo(methodOn(OrderController.class)
                .createOrder(new User(), 0))
                .withRel(() -> "create order"));

        collectionModelUser
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

        assertEquals(collectionModelUser.getContent().stream().toList(),
                userHateoasMapper.getUserByIdHateoas(userForTest).getContent().stream().toList());
    }

    @Test
    void getUserByIdAdminToolHateoasTest() {
        User user = User.builder().id(1L).email("email").build();
        User userForTest = User.builder().id(1L).email("email").build();

        CollectionModel<User> collectionModelUser = CollectionModel.of(List.of(user));
        user.add(linkTo(methodOn(OrderController.class)
                .getOrdersByUserId(new User(), 0, 10))
                .withRel(() -> "get user orders"));
        user.add(linkTo(methodOn(OrderController.class)
                .createOrder(new User(), 0))
                .withRel(() -> "create order"));

        collectionModelUser
                .add(linkTo(methodOn(OrderController.class)
                        .getAllOrders(0, 10))
                        .withRel(() -> "get all orders"))
                .add(linkTo(methodOn(UserController.class)
                        .getAllUsers(0, 10))
                        .withRel(() -> "get all users"))
                .add(linkTo(methodOn(OrderController.class)
                        .getOrdersByUserIdAdminTool(1, 0, 10))
                        .withRel(() -> "get user orders (Admin Tool)"));

        assertEquals(collectionModelUser.getContent().stream().toList(),
                userHateoasMapper.getUserByIdAdminToolHateoas(userForTest).getContent().stream().toList());
    }
}