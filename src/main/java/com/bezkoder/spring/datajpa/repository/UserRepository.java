package com.bezkoder.spring.datajpa.repository;

import com.bezkoder.spring.datajpa.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    Users findByUsername(String username);

    @Query("SELECT u.salt FROM Users u WHERE u.username = :username")
    Optional<String> findSaltByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}
