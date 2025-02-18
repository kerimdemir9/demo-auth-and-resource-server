package com.resserver.demo.resourceserver.data.model;

import com.resserver.demo.resourceserver.RoleListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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
    
    private Date created;

    @Column(columnDefinition = "json")
    @Convert(converter = RoleListConverter.class)
    private List<String> role;
}


