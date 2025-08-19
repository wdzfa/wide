package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPagingAndSortingRepository extends PagingAndSortingRepository<Product, Long> {
}
