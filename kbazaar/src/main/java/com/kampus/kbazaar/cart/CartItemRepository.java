package com.kampus.kbazaar.cart;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<List<CartItem>> findByUsername(String username);
    Optional <CartItem> findCartItemByUsernameAndSku(String username, String sku);
    Optional<Cart> findByUsernameAndSku(String username, String sku);
}
