package com.campus.secondhand.repository;

import com.campus.secondhand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByPhone(String phone);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByPhone(String phone);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.username = ?1 OR u.phone = ?1 OR u.email = ?1")
    Optional<User> findByUsernameOrPhoneOrEmail(String keyword);
}
