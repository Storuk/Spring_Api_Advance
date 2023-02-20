package com.epam.esm.user;

import com.epam.esm.exceptions.ItemNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepoMock;
    @InjectMocks
    private UserService userServiceMock;

    @Test
    void getUserByIdTest_WhenUserExists() {
        when(userRepoMock.findById(1L)).thenReturn(Optional.of(new User()));
        assertEquals(new User(), userServiceMock.getUserById(1L));
    }

    @Test
    void getUserByIdTest_ItemNotFoundException_WhenUserNotExists() {
        long user_id = 1L;
        when(userRepoMock.findById(user_id)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> userServiceMock.getUserById(1L));
        assertEquals("There are no user with id = " + user_id, exception.getMessage());
    }

    @Test
    void getAllUsers() {
        Page<User> users = new PageImpl<>(List.of(new User()));
        when(userRepoMock.findAll(PageRequest.of(0, 3))).thenReturn(users);
        assertEquals(users, userServiceMock.getAllUsers(0, 3));
    }
}