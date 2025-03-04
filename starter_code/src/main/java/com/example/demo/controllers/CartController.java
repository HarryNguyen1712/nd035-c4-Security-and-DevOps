package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import java.util.Optional;
import java.util.stream.IntStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  public static final Logger log = LogManager.getLogger(CartController.class);

  @Autowired private UserRepository userRepository;

  @Autowired private CartRepository cartRepository;

  @Autowired private ItemRepository itemRepository;

  @PostMapping("/addToCart")
  public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
    log.info("CartController.addToCart start");
    User user = userRepository.findByUsername(request.getUsername());
    if (user == null) {
      log.error("user does not exist");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Optional<Item> item = itemRepository.findById(request.getItemId());
    if (!item.isPresent()) {
      log.error("Item does not exist");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Cart cart = user.getCart();
    IntStream.range(0, request.getQuantity()).forEach(i -> cart.addItem(item.get()));
    Cart response = cartRepository.save(cart);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/removeFromCart")
  public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
    log.info("CartController.removeFromCart start");
    User user = userRepository.findByUsername(request.getUsername());
    if (user == null) {
      log.error("user does not exist");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Optional<Item> item = itemRepository.findById(request.getItemId());
    if (!item.isPresent()) {
      log.error("Item does not exist");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Cart cart = user.getCart();
    IntStream.range(0, request.getQuantity()).forEach(i -> cart.removeItem(item.get()));
    Cart response = cartRepository.save(cart);
    return ResponseEntity.ok(response);
  }
}
