package mongodb.api_rest.containerDB;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

public class MongoDBContainerBaseIntTest {

    @ServiceConnection
    protected static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:latest");


    static {
        mongoContainer.start();
    }



}
