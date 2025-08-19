package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Cart;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.id = :id")
    public Cart findByCartId(@PathParam("id") Long id);
}
