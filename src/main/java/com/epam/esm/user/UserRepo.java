package com.epam.esm.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Vlad Storoshchuk
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String name);
}