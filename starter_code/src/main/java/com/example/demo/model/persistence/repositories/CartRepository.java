package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Cart findByUser(User user);
}
