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
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class CartControllerTest {

  private UserRepository userRepository = mock(UserRepository.class);

  private ItemRepository itemRepository = mock(ItemRepository.class);

  private CartRepository cartRepository = mock(CartRepository.class);

  private CartController cartController;

  @Before
  public void setUp() {
    cartController = new CartController();
    TestUtils.injectObjects(cartController, "userRepository", userRepository);
    TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
  }

  @Test
  public void testAddToCart() {
    User user = createUserForTestAdd();
    Item item = createItem();
    Cart cart = createCartForTestAdd();
    when(userRepository.findByUsername("huyna1")).thenReturn(user);
    when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    when(cartRepository.save(any())).thenReturn(cart);

    ModifyCartRequest modifyCartRequest = createModifyCartRequest();
    final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
    Cart responseBody = response.getBody();
    assertNotNull(responseBody);
    assertEquals(cart.getItems().size(), responseBody.getItems().size());
  }

  @Test
  public void testRemoveFromCart() {
    User user = createUserForTestRemove();
    Item item = createItem();
    Cart cart = createCartForTestRemove();
    when(userRepository.findByUsername("huyna1")).thenReturn(user);
    when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    when(cartRepository.save(any())).thenReturn(cart);

    ModifyCartRequest modifyCartRequest = createModifyCartRequest();
    final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
    Cart responseBody = response.getBody();
    assertNotNull(responseBody);
    assertEquals(cart.getItems().size(), responseBody.getItems().size());
  }

  private ModifyCartRequest createModifyCartRequest() {
    ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
    modifyCartRequest.setUsername("huyna1");
    modifyCartRequest.setQuantity(1);
    modifyCartRequest.setItemId(1L);
    return modifyCartRequest;
  }

  private Cart createCartForTestAdd() {
    List<Item> items = createListItem();
    Cart cart = new Cart();
    cart.setId(1L);
    cart.setItems(items);
    cart.setTotal(BigDecimal.valueOf(2));
    return cart;
  }

  private Cart createCartForTestRemove() {
    List<Item> items = new ArrayList<>();
    Cart cart = new Cart();
    cart.setId(1L);
    cart.setItems(items);
    cart.setTotal(BigDecimal.valueOf(0));
    return cart;
  }

  private List<Item> createListItem() {
    Item item1 = new Item();
    item1.setId(1L);
    item1.setName("Item 1");
    item1.setPrice(BigDecimal.valueOf(1000));
    item1.setDescription("Item 1");
    List<Item> items = new ArrayList<>();
    items.add(item1);
    return items;
  }

  private User createUserForTestAdd() {
    Cart cart = new Cart();
    User user = new User();
    user.setId(1L);
    user.setUsername("huyna1");
    user.setPassword("thisIsHashed");
    user.setCart(cart);
    cart.setUser(user);
    return user;
  }

  private User createUserForTestRemove() {
    Cart cart = createCartForTestAdd();
    User user = new User();
    user.setId(1L);
    user.setUsername("huyna1");
    user.setPassword("thisIsHashed");
    user.setCart(cart);
    cart.setUser(user);
    return user;
  }

  private Item createItem() {
    Item item1 = new Item();
    item1.setId(1L);
    item1.setName("Item 1");
    item1.setPrice(BigDecimal.valueOf(1000));
    item1.setDescription("Item 1");
    return item1;
  }
}
