package com.antoszj.testcontainers.demo.repository;

import com.antoszj.testcontainers.demo.DemoApplication;
import com.antoszj.testcontainers.demo.entity.Book;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = DemoApplication.class)
@Testcontainers(disabledWithoutDocker = true)
public class BookRepositoryTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("demo-app-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void dataShouldBeAvailableInDB() {
        System.out.println(postgreSQLContainer.getJdbcUrl());
        List<Book> result = bookRepository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @Ignore
    public void shouldStore() {
        Book book = new Book();
        book.setAuthor("author_" + UUID.randomUUID());
        book.setTitle("title_" + UUID.randomUUID());

        bookRepository.save(book);

        List<Book> result = bookRepository.findAll();
        System.out.println("number of books " + result.size());
        assertThat(result.size()).isEqualTo(3);
    }

}
