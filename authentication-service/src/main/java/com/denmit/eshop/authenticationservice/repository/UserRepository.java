package com.denmit.eshop.authenticationservice.repository;

import com.denmit.eshop.authenticationservice.model.User;
import com.denmit.eshop.authenticationservice.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByEmail(String email);
}
