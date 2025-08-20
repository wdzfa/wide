package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Product;
import com.wdzfa.wide.model.User;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<Product> findProductByName(@PathParam("name") String name);

}
