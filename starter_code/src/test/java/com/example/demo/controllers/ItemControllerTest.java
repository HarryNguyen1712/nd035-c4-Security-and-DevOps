package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class ItemControllerTest {

  private ItemRepository itemRepository = mock(ItemRepository.class);

  private ItemController itemController;

  @Before
  public void setUp() {
    itemController = new ItemController();
    TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
  }

  @Test
  public void testGetItems() {
    List<Item> items = createListItem();
    when(itemRepository.findAll()).thenReturn(items);
    final ResponseEntity<List<Item>> response = itemController.getItems();
    List<Item> responseBody = response.getBody();
    assertNotNull(responseBody);
    assertEquals(items.size(), responseBody.size());
  }

  @Test
  public void testGetItemById() {
    Item item = createItem();
    when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    final ResponseEntity<Item> response = itemController.getItemById(1L);
    Item responseBody = response.getBody();
    assertNotNull(responseBody);
  }

  @Test
  public void testGetItemsByName() {
    List<Item> items = createListItem2();
    when(itemRepository.findByName("Item 1")).thenReturn(items);
    final ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");
    List<Item> responseBody = response.getBody();
    assertNotNull(responseBody);
    assertEquals(items.size(), responseBody.size());
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

  private List<Item> createListItem2() {
    Item item1 = new Item();
    item1.setId(1L);
    item1.setName("Item 1");
    item1.setPrice(BigDecimal.valueOf(1000));
    item1.setDescription("Item 1");

    Item item2 = new Item();
    item1.setId(2L);
    item1.setName("Item 1");
    item1.setPrice(BigDecimal.valueOf(1000));
    item1.setDescription("Item 2");
    List<Item> items = new ArrayList<>();
    items.add(item1);
    items.add(item2);
    return items;
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
