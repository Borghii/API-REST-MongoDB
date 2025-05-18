package mongodb.api_rest.repository;

import mongodb.api_rest.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByGenre(String genre);
    List<Book> findByAuthorId(String authorId);

    boolean existsByTitle(String title);
}
