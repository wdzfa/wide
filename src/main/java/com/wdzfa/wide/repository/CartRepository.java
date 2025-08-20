package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Cart;
import com.wdzfa.wide.model.Product;
import com.wdzfa.wide.model.User;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.id = :id")
    public Cart findByCartId(@PathParam("id") Long id);

    @Query("SELECT c FROM Cart c WHERE c.user = :user AND c.product = :product")
    Optional<Cart> findCartByUserAndProduct(@PathParam("user") User user, @PathParam("product") Product product);

}
