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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthorControllerIT extends MongoDBContainerBaseIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void shouldCreateAuthorSuccessfully() throws Exception {
        // GIVEN
        String requestBody = """
                {
                    "name": "J.R.R. Tolkien",
                    "nationality": "British"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(post("/api/authors")
                        .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("J.R.R. Tolkien"))
                .andExpect(jsonPath("$.nationality").value("British"));
    }

    @Test
    void shouldReturnBooksByAuthor() throws Exception {
        // GIVEN
        Author author = authorRepository.save(Author.builder()
                .name("George Orwell")
                .nationality("British")
                .build());

        Book book1 = bookRepository.save(Book.builder()
                .title("1984")
                .genre("Dystopian")
                .year(1949)
                .authorId(author.getId())
                .build());

        Book book2 = bookRepository.save(Book.builder()
                .title("Animal Farm")
                .genre("Satire")
                .year(1945)
                .authorId(author.getId())
                .build());

        // WHEN & THEN
        mockMvc.perform(get("/api/authors/" + author.getId() + "/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("1984"))
                .andExpect(jsonPath("$[1].title").value("Animal Farm"));
    }
}
