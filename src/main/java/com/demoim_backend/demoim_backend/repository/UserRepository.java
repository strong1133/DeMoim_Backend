package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   User findByUsername(String username);
}
