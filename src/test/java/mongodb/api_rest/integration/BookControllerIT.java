package mongodb.api_rest.integration;

import mongodb.api_rest.containerDB.MongoDBContainerBaseIntTest;
import mongodb.api_rest.entity.Author;
import mongodb.api_rest.entity.Book;
import mongodb.api_rest.repository.AuthorRepository;
import mongodb.api_rest.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookControllerIT extends MongoDBContainerBaseIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private Author savedAuthor;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        savedAuthor = authorRepository.save(Author.builder()
                .name("Aldous Huxley")
                .nationality("British")
                .build());
    }

    @Test
    void shouldCreateBook() throws Exception {
        // GIVEN
        String requestBody = """
                {
                    "title": "Brave New World",
                    "genre": "Dystopian",
                    "year": 1932,
                    "authorId": "%s"
                }
                """.formatted(savedAuthor.getId());

        // WHEN & THEN
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Brave New World"))
                .andExpect(jsonPath("$.genre").value("Dystopian"))
                .andExpect(jsonPath("$.year").value(1932));
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        // GIVEN
        bookRepository.save(Book.builder()
                .title("Island")
                .genre("Philosophical")
                .year(1962)
                .authorId(savedAuthor.getId())
                .build());

        // WHEN & THEN
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Island"));
    }

    @Test
    void shouldGetBooksByGenre() throws Exception {
        // GIVEN
        bookRepository.save(Book.builder()
                .title("Island")
                .genre("Philosophical")
                .year(1962)
                .authorId(savedAuthor.getId())
                .build());

        // WHEN & THEN
        mockMvc.perform(get("/api/books")
                        .param("genre", "Philosophical"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].genre").value("Philosophical"));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        // GIVEN
        Book existingBook = bookRepository.save(Book.builder()
                .title("Original Title")
                .genre("Drama")
                .year(1950)
                .authorId(savedAuthor.getId())
                .build());

        String updatedRequest = """
                {
                    "title": "Updated Title",
                    "genre": "Drama",
                    "year": 1951,
                    "authorId": "%s"
                }
                """.formatted(savedAuthor.getId());

        // WHEN & THEN
        mockMvc.perform(put("/api/books/" + existingBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.year").value(1951));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        // GIVEN
        Book bookToDelete = bookRepository.save(Book.builder()
                .title("To Delete")
                .genre("Horror")
                .year(1970)
                .authorId(savedAuthor.getId())
                .build());

        // WHEN & THEN
        mockMvc.perform(delete("/api/books/" + bookToDelete.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("To Delete"));
    }

    @Test
    void shouldCreateMultipleBooks() throws Exception {
        // GIVEN
        String booksJson = """
                [
                    {
                        "title": "Book One",
                        "genre": "Sci-Fi",
                        "year": 2000,
                        "authorId": "%s"
                    },
                    {
                        "title": "Book Two",
                        "genre": "Sci-Fi",
                        "year": 2001,
                        "authorId": "%s"
                    }
                ]
                """.formatted(savedAuthor.getId(), savedAuthor.getId());

        // WHEN & THEN
        mockMvc.perform(post("/api/books/list")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(booksJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book One"))
                .andExpect(jsonPath("$[1].title").value("Book Two"));
    }
}
