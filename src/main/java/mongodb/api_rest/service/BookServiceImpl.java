package mongodb.api_rest.service;

import mongodb.api_rest.entity.Book;
import mongodb.api_rest.exception.DuplicateBookException;
import mongodb.api_rest.repository.AuthorRepository;
import mongodb.api_rest.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book createBook(Book book) {

        existsAuthor(book.getAuthorId());
        existsBook(book);

        return bookRepository.save(book);
    }

    @Override
    public List<Book> createBooks(List<Book> books) {

        return books.stream()
                .map(book -> {
                    existsAuthor(book.getAuthorId());
                    existsBook(book);
                    return bookRepository.save(book);
                }).toList();

    }

    private void existsBook(Book book) {
        if (bookRepository.existsByTitle(book.getTitle())){
            throw new DuplicateBookException("The book with title: "+ book.getTitle()+" already exist");
        }
    }
    private void existsAuthor(String authorId) {
        if (!authorRepository.existsById(authorId)){
            throw new NoSuchElementException("Author not found with id: " + authorId);
        }
    }


    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }



    @Override
    public Book updateBook(Book book, String id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));

        existsAuthor(book.getAuthorId());

        existingBook.setTitle(book.getTitle());
        existingBook.setGenre(book.getGenre());
        existingBook.setYear(book.getYear());
        existingBook.setAuthorId(book.getAuthorId());

        return bookRepository.save(existingBook);
    }



    @Override
    public Book deleteBook(String id) {
        Book deletedBook = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
        bookRepository.delete(deletedBook);
        return deletedBook;
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public List<Book> findByIdAuthor(String idAuthor) {

        existsAuthor(idAuthor);

        return bookRepository.findByAuthorId(idAuthor);
    }



}
