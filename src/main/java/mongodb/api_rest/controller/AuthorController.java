package mongodb.api_rest.controller;

import mongodb.api_rest.entity.Author;
import mongodb.api_rest.entity.Book;
import mongodb.api_rest.service.AuthorService;
import mongodb.api_rest.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @PostMapping
    ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.createAuthor(author), HttpStatus.CREATED);
    }


    @GetMapping("/{id}/books")
    ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String id) {
        return new ResponseEntity<>(bookService.findByIdAuthor(id), HttpStatus.OK);
    }


}
