package com.epam.esm.orders;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    private MockMvc mvc;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderHateoasMapper orderHateoasMapper;
    private JacksonTester<Map<String, CollectionModel<?>>> jsonOrderCollectionModel;
    private JacksonTester<Map<String, PagedModel<?>>> jsonOrderPagedModel;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new ControllerAdvisor())
                .build();
    }

    @Test
    void createOrder() throws Exception {
        Order order = new Order();
        when(orderService.createOrder(1L, 1L)).thenReturn(new Order());
        when(orderHateoasMapper.createOrderHateoasMapper(order)).thenReturn(CollectionModel.of(List.of(order)));
        MockHttpServletResponse response = mvc.perform(post("/orders/1/1").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonOrderCollectionModel.write(Map.of("order", CollectionModel.of(List.of(order)))).getJson(), response.getContentAsString());
    }

    @Test
    void getOrdersByUserId() throws Exception {
        Page<Order> orderPage = Page.empty();
        when(orderService.getOrdersByUserId(1L, 0, 10)).thenReturn(orderPage);
        when(orderHateoasMapper.getUserOrdersHateoasMapper(orderPage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/orders/by-user-id/1?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonOrderPagedModel.write(Map.of("userOrders", PagedModel.empty())).getJson(), response.getContentAsString());
    }

    @Test
    void getAllOrders() throws Exception {
        Page<Order> orderPage = Page.empty();
        when(orderService.getAllOrders(0, 10)).thenReturn(orderPage);
        when(orderHateoasMapper.getAllOrdersHateoasMapper(orderPage)).thenReturn(PagedModel.empty());
        MockHttpServletResponse response = mvc.perform(get("/orders?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(jsonOrderPagedModel.write(Map.of("orders", PagedModel.empty())).getJson(), response.getContentAsString());
    }
}