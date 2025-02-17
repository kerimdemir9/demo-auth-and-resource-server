package com.resserver.demo.resourceserver;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "auth_db", name = "books")
@Entity
@Builder
public class BookModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String author;
}
