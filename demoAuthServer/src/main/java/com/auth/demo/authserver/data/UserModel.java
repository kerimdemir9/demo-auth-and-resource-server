package com.auth.demo.authserver.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "auth_db", name = "user")
@Entity
@Builder
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(columnDefinition = "json")
    @Convert(converter = RoleListConverter.class)
    private List<String> role;
}


