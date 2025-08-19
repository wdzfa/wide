package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByName(String name);
}
