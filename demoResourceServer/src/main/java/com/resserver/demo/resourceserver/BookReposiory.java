package com.resserver.demo.resourceserver;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReposiory extends JpaRepository<BookModel, Integer> {
    List<BookModel> findAllByAuthor(String author);
}
