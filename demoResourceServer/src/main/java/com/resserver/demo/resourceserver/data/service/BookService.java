package com.resserver.demo.resourceserver.data.service;

import com.resserver.demo.resourceserver.data.model.BookModel;
import com.resserver.demo.resourceserver.data.repository.BookReposiory;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    final BookReposiory bookReposiory;

    @Autowired
    public BookService(BookReposiory bookReposiory) {
        this.bookReposiory = bookReposiory;
    }

    @Cacheable(value = "book", key = "#author")
    public List<BookModel> findAllByAuthor(String author) {
        try {
            val found = bookReposiory.findAllByAuthor(author);
            if (found.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books found for author: " + author);
            }
            return found;
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", e);
        }
    }
}
