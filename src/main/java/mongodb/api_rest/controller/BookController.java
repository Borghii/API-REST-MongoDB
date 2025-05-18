package mongodb.api_rest.controller;

import mongodb.api_rest.entity.Book;
import mongodb.api_rest.service.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookServiceImpl bookService;

    @Autowired
    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String genre) {

        if (genre != null && !genre.isEmpty()) {
            return new ResponseEntity<>(bookService.findByGenre(genre), HttpStatus.OK);
        }

        return new ResponseEntity<>(bookService.findAllBooks(), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable String id) {
        return new ResponseEntity<>(bookService.updateBook(book, id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<List<Book>> createBooks(@RequestBody List<Book> books) {
        List<Book> createdBooks = bookService.createBooks(books);
        return new ResponseEntity<>(createdBooks, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable String id) {
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }


}
