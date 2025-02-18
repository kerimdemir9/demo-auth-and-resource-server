package com.resserver.demo.resourceserver.data.repository;

import com.resserver.demo.resourceserver.data.model.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReposiory extends JpaRepository<BookModel, Integer> {
    List<BookModel> findAllByAuthor(String author);
}
