package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
   private OrderRepository orderRepository;

  public static final Logger log = LoggerFactory.getLogger(OrderController.class);

  @PostMapping("/submit/{username}")
  public ResponseEntity<UserOrder> submit(@PathVariable String username) {
    log.info("OrderController.submit start");
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    UserOrder order = UserOrder.createFromCart(user.getCart());
    UserOrder userOrder = orderRepository.save(order);
    return ResponseEntity.ok(userOrder);
  }

  @GetMapping("/history/{username}")
  public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
    log.info("OrderController.submit start");
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return ResponseEntity.ok(orderRepository.findByUser(user));
  }
}
