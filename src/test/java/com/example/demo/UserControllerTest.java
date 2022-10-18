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

    // ToDo: Find out why attempts to use @InjectMocks and @Mock (as used in other tests) did not work so far
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void testCreateUser(){
        // manipulate the hashing feature so that the result is known beforehand
        when(encoder.encode(TestUtils.PASSWORD)).thenReturn(TestUtils.PASSWORD_HASHED);
        // create a user with specs above and fetch response
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(TestUtils.USER_NAME);
        request.setPassword(TestUtils.PASSWORD);
        request.setConfirmPassword(TestUtils.PASSWORD);
        // send the request and check for proper ok-response
        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        // get user object from response and compare with the input specs
        User actualUser = response.getBody();
        assertNotNull(actualUser);
        assertEquals(0, actualUser.getId());
        assertEquals(TestUtils.USER_NAME, actualUser.getUsername());
        assertEquals(TestUtils.PASSWORD_HASHED, actualUser.getPassword());
    }

    @Test
    public void testFindUserById(){
        // create a requestedUser object and tell the requestedUser repository mock to respond with it
        User requestedUser = TestUtils.createUser();
        when(userRepository.findById(requestedUser.getId())).thenReturn(Optional.of(requestedUser));
        // send the request and check for proper ok-response
        ResponseEntity<User> response = userController.findById(requestedUser.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        // get requested user object from response and compare with the input specs
        User actualUser = response.getBody();
        assertNotNull(actualUser);
        assertEquals(requestedUser.getId(), actualUser.getId());
        assertEquals(requestedUser.getUsername(), actualUser.getUsername());
        assertEquals(requestedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    public void testFindUserByName(){
        // create a requestedUser object and tell the requestedUser repository mock to respond with it
        User requestedUser = TestUtils.createUser();
        when(userRepository.findByUsername(requestedUser.getUsername())).thenReturn(requestedUser);
        // send the request and check for proper ok-response
        ResponseEntity<User> response = userController.findByUserName(requestedUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        // get requested user object from response and compare with the input specs
        User actualUser = response.getBody();
        assertNotNull(actualUser);
        assertEquals(requestedUser.getId(), actualUser.getId());
        assertEquals(requestedUser.getUsername(), actualUser.getUsername());
        assertEquals(requestedUser.getPassword(), actualUser.getPassword());
    }

}
