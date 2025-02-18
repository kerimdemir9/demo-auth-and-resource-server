package com.resserver.demo.resourceserver;

import com.resserver.demo.resourceserver.data.model.BookModel;
import com.resserver.demo.resourceserver.data.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GetMapping("/book_author")
    public ResponseEntity<List<BookModel>> getBookByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.findAllByAuthor(author));
    }
    
}
