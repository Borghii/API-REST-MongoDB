package mongodb.api_rest.service;

import mongodb.api_rest.entity.Author;
import mongodb.api_rest.entity.Book;
import mongodb.api_rest.exception.DuplicateBookException;
import mongodb.api_rest.repository.AuthorRepository;
import mongodb.api_rest.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat; // Correcto


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .id("author-123")
                .name("George Orwell")
                .nationality("British")
                .build();
    }

    @Test
    void shouldCreateBookSuccessfully() {
        // GIVEN
        Book book = Book.builder()
                .title("1984")
                .genre("Dystopian")
                .year(1949)
                .authorId(author.getId())
                .build();

        when(authorRepository.existsById(author.getId())).thenReturn(true);
        when(bookRepository.existsByTitle("1984")).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(book);

        // WHEN
        Book result = bookService.createBook(book);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("1984");
        verify(bookRepository).save(book);
    }

    @Test
    void shouldThrowWhenDuplicateBookTitle() {
        // GIVEN
        Book book = Book.builder()
                .title("1984")
                .genre("Dystopian")
                .year(1949)
                .authorId(author.getId())
                .build();

        when(authorRepository.existsById(author.getId())).thenReturn(true);
        when(bookRepository.existsByTitle("1984")).thenReturn(true);

        // WHEN & THEN
        assertThatThrownBy(() -> bookService.createBook(book))
                .isInstanceOf(DuplicateBookException.class)
                .hasMessageContaining("already exist");
    }

    @Test
    void shouldThrowWhenAuthorNotFound() {
        // GIVEN
        Book book = Book.builder()
                .title("Animal Farm")
                .genre("Satire")
                .year(1945)
                .authorId("non-existent-id")
                .build();

        when(authorRepository.existsById("non-existent-id")).thenReturn(false);

        // WHEN & THEN
        assertThatThrownBy(() -> bookService.createBook(book))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Author not found");
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        // GIVEN
        Book existingBook = Book.builder()
                .id("book-001")
                .title("Old Title")
                .genre("Old Genre")
                .year(1930)
                .authorId(author.getId())
                .build();

        Book update = Book.builder()
                .title("New Title")
                .genre("New Genre")
                .year(1950)
                .authorId(author.getId())
                .build();

        when(bookRepository.findById("book-001")).thenReturn(Optional.of(existingBook));
        when(authorRepository.existsById(author.getId())).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        Book result = bookService.updateBook(update, "book-001");

        // THEN
        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getGenre()).isEqualTo("New Genre");
        assertThat(result.getYear()).isEqualTo(1950);
    }

    @Test
    void shouldDeleteBookSuccessfully() {
        // GIVEN
        Book book = Book.builder()
                .id("book-123")
                .title("1984")
                .genre("Dystopian")
                .year(1949)
                .authorId(author.getId())
                .build();

        when(bookRepository.findById("book-123")).thenReturn(Optional.of(book));

        // WHEN
        Book deleted = bookService.deleteBook("book-123");

        // THEN
        assertThat(deleted).isEqualTo(book);
        verify(bookRepository).delete(book);
    }

    @Test
    void shouldFindBooksByGenre() {
        // GIVEN
        Book book = Book.builder()
                .title("1984")
                .genre("Dystopian")
                .year(1949)
                .authorId(author.getId())
                .build();

        when(bookRepository.findByGenre("Dystopian")).thenReturn(List.of(book));

        // WHEN
        List<Book> result = bookService.findByGenre("Dystopian");

        // THEN
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo("1984");
    }

    @Test
    void shouldFindBooksByAuthorId() {
        // GIVEN
        Book book1 = Book.builder()
                .title("1984")
                .genre("Dystopian")
                .year(1949)
                .authorId(author.getId())
                .build();

        Book book2 = Book.builder()
                .title("Animal Farm")
                .genre("Satire")
                .year(1945)
                .authorId(author.getId())
                .build();

        when(authorRepository.existsById(author.getId())).thenReturn(true);
        when(bookRepository.findByAuthorId(author.getId())).thenReturn(List.of(book1, book2));

        // WHEN
        List<Book> result = bookService.findByIdAuthor(author.getId());

        // THEN
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList()))
                .containsExactlyInAnyOrder("1984", "Animal Farm");
    }
}