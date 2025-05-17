package mongodb.api_rest.repository;

import mongodb.api_rest.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}
