package com.resserver.demo.resourceserver.data.service;

import com.resserver.demo.resourceserver.data.model.UserModel;
import com.resserver.demo.resourceserver.data.repository.UserRepository;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Service
public class UserService {

    final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel getUser(String username) {
        try {
            val found = userRepository.findByUsername(username);
            if (Objects.isNull(found)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
            }
            return found;
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }
    
    public UserModel saveUser(UserModel user) {
        try {
            user.setCreated(new Date(Instant.now().toEpochMilli()));
            return userRepository.save(user);
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }
}

