package mongodb.api_rest.service;

import mongodb.api_rest.entity.Book;
import mongodb.api_rest.repository.BookRepository;

import java.util.List;

public interface BookService {
    Book createBook(Book book);
    List<Book> createBooks(List<Book> books);

    List<Book> findAllBooks();
    Book updateBook(Book book, String id);
    Book deleteBook(String id);
    List<Book> findByGenre(String genre);
    List<Book> findByIdAuthor(String idAuthor);
}
