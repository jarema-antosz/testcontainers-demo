package com.antoszj.testcontainers.demo.repository;

import com.antoszj.testcontainers.demo.DemoApplication;
import com.antoszj.testcontainers.demo.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = DemoApplication.class)
@ContextConfiguration(initializers = {BookRepositoryTest.Initializer.class})
@Testcontainers(disabledWithoutDocker = true)
public class BookRepositoryTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("demo-app-tests-db")
            .withUsername("sa")
            .withPassword("sa").withReuse(true);

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void dataShouldBeAvailableInDB() {
        System.out.println(postgreSQLContainer.getJdbcUrl());
        List<Book> result = bookRepository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }

}
