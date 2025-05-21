package mongodb.api_rest.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "authors")
public class Author {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String nationality;
}
