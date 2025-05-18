package mongodb.api_rest.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @Indexed(unique = true)
    private String title;
    private String genre;
    private int year;
    private String authorId;
}
