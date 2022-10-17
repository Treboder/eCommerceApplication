package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();

        TestHelperMethods.injectObjects(userController, "userRepository", userRepository);
        TestHelperMethods.injectObjects(userController, "cartRepository", cartRepository);
        TestHelperMethods.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);

        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void verify_findById(){
        long id = 1L;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User actualUser = response.getBody();

        assertNotNull(actualUser);

        assertEquals(id, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("testPassword", actualUser.getPassword());
    }

    @Test
    public void verify_findByUserName(){
        long id = 1L;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User actualUser = response.getBody();

        assertNotNull(actualUser);

        assertEquals(id, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("testPassword", actualUser.getPassword());
    }

}
