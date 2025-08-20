package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Product;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<Product> findProductByName(@PathParam("name") String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.type) = LOWER(:type)")
    List<Product> findProductByType(@PathParam("type") String type, Pageable pageable);

}
