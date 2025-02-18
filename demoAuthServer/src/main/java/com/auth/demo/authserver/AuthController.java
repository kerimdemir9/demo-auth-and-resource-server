package com.auth.demo.authserver;

import com.auth.demo.authserver.data.RefreshRequest;
import com.auth.demo.authserver.data.UserModel;
import com.auth.demo.authserver.data.UserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new UsernameNotFoundException("Invalid username/password");
        }
        val user = userService.findByUsername(authRequest.getUsername());

        String accessToken = generateToken(authRequest.getUsername(), 1);  // Access token for 1 hour
        String refreshToken = generateToken(authRequest.getUsername(), 24);  // Refresh token for 24 hours

        user.setRefreshToken(refreshToken);
        userService.saveUser(user);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, 3600));
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        val user = userService.findByRefreshToken(refreshRequest.getRefreshToken());

        String newAccessToken = generateToken(user.getUsername(), 1);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, user.getRefreshToken(), 3600));
    }

    private String generateToken(String username, int hours) {
        Instant now = Instant.now();
        val user = userService.findByUsername(username);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:9000")
                .issuedAt(now)
                .expiresAt(now.plus(hours, ChronoUnit.HOURS))
                .subject(username)
                .claim("authorities", user.getRole().stream().map(r -> "ROLE_".concat(r)).toList())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
