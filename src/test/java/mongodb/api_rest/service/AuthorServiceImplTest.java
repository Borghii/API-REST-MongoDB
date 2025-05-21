package mongodb.api_rest.service;

import mongodb.api_rest.containerDB.MongoDBContainerBaseIntTest;
import mongodb.api_rest.entity.Author;
import mongodb.api_rest.repository.AuthorRepository;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void shouldCreateBritishAuthorJKRowling() {
        // GIVEN
        Author authorToSave = Author.builder()
                .name("J.K. Rowling")
                .nationality("British")
                .build();

        Author savedAuthor = Author.builder()
                .id("author-123")
                .name("J.K. Rowling")
                .nationality("British")
                .build();

        when(authorRepository.save(authorToSave)).thenReturn(savedAuthor);

        // WHEN
        Author result = authorService.createAuthor(authorToSave);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(Author::getName, Author::getNationality)
                .containsExactly("J.K. Rowling", "British");

        assertThat(result.getId()).isEqualTo("author-123");
        verify(authorRepository).save(authorToSave);
    }
}
