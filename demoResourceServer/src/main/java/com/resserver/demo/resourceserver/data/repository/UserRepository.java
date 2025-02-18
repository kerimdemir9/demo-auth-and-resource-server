package com.resserver.demo.resourceserver.data.repository;

import com.resserver.demo.resourceserver.data.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    UserModel findByRefreshToken(String refreshToken);
}

