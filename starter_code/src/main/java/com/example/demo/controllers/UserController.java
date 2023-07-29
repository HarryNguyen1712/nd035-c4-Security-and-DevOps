package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
  public static final Logger log = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartRepository cartRepository;

  @GetMapping("/id/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    log.info("UserController.findById start");
    Optional<User> user = userRepository.findById(id);
    if (!user.isPresent()) {
      log.error("Can not find user by id: " + id);
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.of(user);
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> findByUserName(@PathVariable String username) {
    log.info("UserController.findByUserName start");
    User user = userRepository.findByUsername(username);
    if (user == null) {
      log.error("Can not find user by username: " + username);
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
  }

  @PostMapping("/create")
  public ResponseEntity<User> createUser(
      @Validated @RequestBody CreateUserRequest createUserRequest) {
    log.info("UserController.createUser start");
    User user = new User();
    user.setUsername(createUserRequest.getUsername());
    user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
    Cart cart = new Cart();
    Cart responseCart = cartRepository.save(cart);
    user.setCart(responseCart);
    User response = userRepository.save(user);
    return ResponseEntity.ok(response);
  }
}
