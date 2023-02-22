package com.epam.esm.user;

import com.epam.esm.exceptions.controlleradvice.ControllerAdvisor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    @Mock
    private UserHateoasMapper userHateoasMapper;
    private JacksonTester<Map<String, CollectionModel<?>>> jsonUserCollectionModel;
    private JacksonTester<Map<String, PagedModel<?>>> jsonUserPagedModel;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ControllerAdvisor())
                .build();
    }

    @Test
    void getAllUsers() throws Exception {
        Page<User> userPage = Page.empty();
        when(userService.getAllUsers(0, 10)).thenReturn(userPage);
        when(userHateoasMapper.getAllUsersHateoas(userPage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/users?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonUserPagedModel.write(Map.of("users", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getUserById() throws Exception {
        User user = User.builder().id(1L).login("user").build();
        when(userService.getUserById(1L)).thenReturn(user);
        when(userHateoasMapper.getUserByIdHateoas(user)).thenReturn(CollectionModel.of(List.of(user)));
        MockHttpServletResponse response = mvc.perform(get("/users/1")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonUserCollectionModel.write(Map.of("user", CollectionModel.of(List.of(user)))).getJson(), response.getContentAsString());
    }
}