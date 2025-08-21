package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Product;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductRepository extends CrudRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<Product> findProductByName(@PathParam("name") String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.type) = LOWER(:type)")
    Page<Product> findProductByType(@Param("type") String type, Pageable pageable);


}
