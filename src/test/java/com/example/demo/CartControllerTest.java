package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.demo.TestHelperMethods.createItem;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup(){

        when(userRepository.findByUsername("fymo")).thenReturn(TestHelperMethods.createUser());
        when(itemRepository.findById(any())).thenReturn(Optional.of(createItem(1)));

    }

    @Test
    public void verify_addToCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("fymo");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();

        Cart generatedCart = TestHelperMethods.createCart(TestHelperMethods.createUser());

        assertNotNull(actualCart);

        Item item = createItem(request.getItemId());
        BigDecimal itemPrice = item.getPrice();

        BigDecimal expectedTotal = itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())).add(generatedCart.getTotal());

        assertEquals("fymo", actualCart.getUser().getUsername());
        assertEquals(generatedCart.getItems().size() + request.getQuantity(), actualCart.getItems().size());
        assertEquals(createItem(1), actualCart.getItems().get(0));
        assertEquals(expectedTotal, actualCart.getTotal());

        verify(cartRepository, times(1)).save(actualCart);

    }

    @Test
    public void verify_removeFromCart(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("fymo");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();

        Cart generatedCart = TestHelperMethods.createCart(TestHelperMethods.createUser());

        assertNotNull(actualCart);

        Item item = createItem(request.getItemId());
        BigDecimal itemPrice = item.getPrice();

        BigDecimal expectedTotal = generatedCart.getTotal().subtract(itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())));

        assertEquals("fymo", actualCart.getUser().getUsername());
        assertEquals(generatedCart.getItems().size() - request.getQuantity(), actualCart.getItems().size());
        assertEquals(createItem(2), actualCart.getItems().get(0));
        assertEquals(expectedTotal, actualCart.getTotal());

        verify(cartRepository, times(1)).save(actualCart);

    }

    @Test
    public void verify_InvalidUsername(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("invalidUser");

        ResponseEntity<Cart> removeResponse = cartController.removeFromcart(request);
        assertNotNull(removeResponse);
        assertEquals(404, removeResponse.getStatusCodeValue());
        assertNull(removeResponse.getBody());

        ResponseEntity<Cart> addResponse = cartController.addTocart(request);
        assertNotNull(addResponse);
        assertEquals(404, addResponse.getStatusCodeValue());
        assertNull(addResponse.getBody());

        verify(userRepository, times(2)).findByUsername("invalidUser");

    }



}
