package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
