package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import java.util.List;
import java.util.Optional;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item")
public class ItemController {

  public static final Logger log = LogManager.getLogger(ItemController.class);

  @Autowired private ItemRepository itemRepository;

  @GetMapping
  public ResponseEntity<List<Item>> getItems() {
    log.info("ItemController.getItems start");
    return ResponseEntity.ok(itemRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> getItemById(@PathVariable Long id) {
    log.info("ItemController.getItemById start");
    Optional<Item> item = itemRepository.findById(id);
    if (!item.isPresent()) {
      log.info("Can not find item by id: " + id);
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.of(itemRepository.findById(id));
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
    log.info("ItemController.getItemById start");
    List<Item> items = itemRepository.findByName(name);
    if (items == null || items.isEmpty()) {
      log.info("can not find items by name: " + name);
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(items);
  }
}
