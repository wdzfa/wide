package com.wdzfa.wide.repository;

import com.wdzfa.wide.model.User;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT p FROM User p WHERE LOWER(p.name) = LOWER(:name)")
    Optional<User> findUserByName(@PathParam("name") String name);

}
