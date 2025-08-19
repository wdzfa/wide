package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Product;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Optional<Product> findProductByName(@PathParam("name") String name);

    @Query("SELECT p FROM Product p WHERE p.type = :type")
    Optional<Product> findProductByType(@PathParam("type") String type);

    @Query("SELECT p FROM Product p WHERE p.id = :id")
    public Product findByProductId(@PathParam("id") Long id);
}
