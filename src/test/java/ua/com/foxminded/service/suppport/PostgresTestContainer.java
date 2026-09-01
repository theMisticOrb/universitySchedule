package ua.com.foxminded.service.suppport;

import org.testcontainers.containers.PostgreSQLContainer;

public final class PostgresTestContainer {
    
    private static final PostgreSQLContainer<?> INSTANCE = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db").withUsername("test").withPassword("test").withReuse(true);;

    static {
        INSTANCE.start();
    }

    private PostgresTestContainer() {
    }

    public static PostgreSQLContainer<?> getInstance() {
        return INSTANCE;
    }
}
