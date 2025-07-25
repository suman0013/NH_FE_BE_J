package com.namhatta.repository;

import com.namhatta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUsernameAndActive(String username, Boolean active);
    
    @Query("SELECT u FROM User u JOIN FETCH u.userDistricts WHERE u.username = :username")
    Optional<User> findByUsernameWithDistricts(@Param("username") String username);
    
    boolean existsByUsername(String username);
}