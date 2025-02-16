package com.auth.demo.authserver.data;

import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel saveUser(UserModel user) {
        try {
            return userRepository.save(user);
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            val user = userRepository.findByUsername(username);
            if (Objects.isNull(user)) {
                throw new UsernameNotFoundException("User with username: "
                        .concat(username).concat(" not found"));
            }
            return User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRole().stream()
                            .map(role -> "ROLE_" + role)
                            .toArray(String[]::new))
                    .build();
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public UserModel findByUsername(String username) {
        try {
            val user = userRepository.findByUsername(username);
            if (Objects.isNull(user)) {
                throw new UsernameNotFoundException("User with username: "
                        .concat(username).concat(" not found"));
            }
            return user;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public UserModel findByRefreshToken(String refreshToken) {
        try {
            val user = userRepository.findByRefreshToken(refreshToken);
            if (Objects.isNull(user)) {
                throw new UsernameNotFoundException("User with refreshToken: "
                        .concat(refreshToken).concat(" not found"));
            }
            return user;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }
}

