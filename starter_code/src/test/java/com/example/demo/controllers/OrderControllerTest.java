package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class OrderControllerTest {

  private UserRepository userRepository = mock(UserRepository.class);

  private OrderRepository orderRepository = mock(OrderRepository.class);

  private OrderController orderController;

  @Before
  public void setUp()  {
    orderController = new OrderController();
    TestUtils.injectObjects(orderController, "userRepository", userRepository);
    TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
  }

  @Test
  public void testSubmit() {
    User user = createUser();
    when(userRepository.findByUsername("huyna1")).thenReturn(user);
    UserOrder userOrder = createOrder(user.getCart());
    when(orderRepository.save(any())).thenReturn(userOrder);
    final ResponseEntity<UserOrder> response = orderController.submit("huyna1");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    UserOrder order = response.getBody();
    assertNotNull(order);
    assertEquals(0L, order.getId().longValue());
    assertEquals(userOrder.getItems().size(), order.getItems().size());
    assertEquals(userOrder.getTotal(), order.getTotal());
    assertEquals(userOrder.getUser().getId(), order.getUser().getId());
  }

  @Test
  public void testGetOrdersForUser() {
    User user = createUser();
    when(userRepository.findByUsername("huyna1")).thenReturn(user);
    List<UserOrder> userOrders = createListUserOrder();
    when(orderRepository.findByUser(any())).thenReturn(userOrders);
    final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("huyna1");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<UserOrder> order = response.getBody();
    assertNotNull(order);
    assertEquals(order.size(), userOrders.size());
  }

  private List<UserOrder> createListUserOrder() {
    User user = createUser();
    UserOrder userOrder1 = createOrder(user.getCart());
    List<UserOrder> userOrders = new ArrayList<>();
    userOrders.add(userOrder1);
    return userOrders;
  }

  private List<Item> createListItem() {
    Item item1 = new Item();
    item1.setId(1L);
    item1.setName("Item 1");
    item1.setPrice(BigDecimal.valueOf(1000));
    item1.setDescription("Item 1");

    Item item2 = new Item();
    item1.setId(2L);
    item1.setName("Item 2");
    item1.setPrice(BigDecimal.valueOf(1000));
    item1.setDescription("Item 2");
    List<Item> items = new ArrayList<>();
    items.add(item1);
    items.add(item2);
    return items;
  }

  private Cart createCart() {
    List<Item> items = createListItem();
    Cart cart = new Cart();
    cart.setId(1L);
    cart.setItems(items);
    cart.setTotal(BigDecimal.valueOf(2));
    return cart;
  }

  private User createUser() {
    Cart cart = createCart();
    User user = new User();
    user.setId(1L);
    user.setUsername("huyna1");
    user.setPassword("thisIsHashed");
    user.setCart(cart);
    cart.setUser(user);
    return user;
  }

  private UserOrder createOrder(Cart cart) {
    UserOrder order = new UserOrder();
    order.setId(0L);
    order.setItems(new ArrayList<>(cart.getItems()));
    order.setTotal(cart.getTotal());
    order.setUser(cart.getUser());
    return order;
  }
}
