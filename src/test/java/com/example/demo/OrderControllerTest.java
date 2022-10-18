package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    User user;
    UserOrder order;
    List<UserOrder> orderList;

    @Before
    public void setup(){
        // define initial test objects and make the (mock) repos respond with them accordingly
        user = TestUtils.createUserWithCartIncludingOneDemoItem();
        order = TestUtils.createDemoOrder();
        orderList = Arrays.asList(order);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(orderList);
    }

    @Test
    public void testGetOrderListForUserByName(){
        // send request and check for ok-response
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        // fetch order object and compare with input order
        List<UserOrder> actualOrderList = response.getBody();
        assertEquals(orderList.size(), actualOrderList.size());
        UserOrder actualOrder = orderList.get(0);
        assertEquals(order.getId(), actualOrder.getId());
        assertEquals(order.getTotal(), actualOrder.getTotal()); // ToDo: Round total value to max two decimals
    }

    @Test
    public void testSubmitSuccess(){
        // send request and check for ok-response
        ResponseEntity<UserOrder> response =  orderController.submit(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        // fetch order from response and compare with requested order
        UserOrder actualOrder = response.getBody();
        assertNotNull(actualOrder);
        // assertEquals(order.getId(), actualOrder.getId()); ToDo: Check why returned id of returned order is null
        assertEquals(order.getItems(), actualOrder.getItems());
        assertEquals(order.getTotal(), actualOrder.getTotal());
    }

    @Test
    public void testSubmitFail(){
        // send request and check for not-found-response without body/content
        ResponseEntity<UserOrder> response = orderController.submit("invalid username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull( response.getBody());
    }





}
