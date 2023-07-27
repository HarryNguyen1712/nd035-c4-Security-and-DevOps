package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired private MockMvc mvc;

  private UserController userController;

  private UserRepository userRepository = mock(UserRepository.class);

  private CartRepository cartRepository = mock(CartRepository.class);

  private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

  @Before
  public void setUp() {
    userController = new UserController();
    TestUtils.injectObjects(userController, "userRepository", userRepository);
    TestUtils.injectObjects(userController, "cartRepository", cartRepository);
    TestUtils.injectObjects(userController, "passwordEncoder", bCryptPasswordEncoder);
  }

  @Test
  public void testCreateUser() {
    when(bCryptPasswordEncoder.encode("password1")).thenReturn("thisIsHashed");
    CreateUserRequest createUserRequest = createUserRequest();
    final ResponseEntity<User> response = userController.createUser(createUserRequest);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    User u = response.getBody();
    assertNotNull(u);
    assertEquals(0, u.getId());
    assertEquals("huyna1", u.getUsername());
    assertEquals("thisIsHashed", u.getPassword());
  }

  @Test
  public void testFindUserById() {
    User user = createUser();
    when(userRepository.findById(0L)).thenReturn(Optional.of(user));
    final ResponseEntity<User> response = userController.findById(0L);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    User u = response.getBody();
    assertNotNull(u);
    assertEquals(1, u.getId());
    assertEquals("huyna1", u.getUsername());
    assertEquals("thisIsHashed", u.getPassword());
  }

  @Test
  public void testFindUserByUsername() {
    User user = createUser();
    when(userRepository.findByUsername("huyna1")).thenReturn(user);
    final ResponseEntity<User> response = userController.findByUserName("huyna1");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    User u = response.getBody();
    assertNotNull(u);
    assertEquals(1, u.getId());
    assertEquals("huyna1", u.getUsername());
    assertEquals("thisIsHashed", u.getPassword());
  }

  @Test
  public void testCreateUserBadRequest() throws Exception {
    when(bCryptPasswordEncoder.encode("password1")).thenReturn("thisIsHashed");
    CreateUserRequest createUserRequest = createUserRequest();
    createUserRequest.setConfirmPassword("test");
    mvc.perform(
            MockMvcRequestBuilders.post("/api/user/create")
                .content(asJsonString(createUserRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  private CreateUserRequest createUserRequest() {
    CreateUserRequest createUserRequest = new CreateUserRequest();
    createUserRequest.setUsername("huyna1");
    createUserRequest.setPassword("password1");
    createUserRequest.setConfirmPassword("password1");
    return createUserRequest;
  }

  private User createUser() {
    User user = new User();
    user.setId(1L);
    user.setUsername("huyna1");
    user.setPassword("thisIsHashed");
    user.setCart(new Cart());
    return user;
  }

  private String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
