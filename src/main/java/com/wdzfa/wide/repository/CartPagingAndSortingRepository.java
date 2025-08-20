package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Cart;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CartPagingAndSortingRepository extends PagingAndSortingRepository<Cart, Long> {
}
