package com.epam.esm.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Vlad Storoshchuk
 * */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}