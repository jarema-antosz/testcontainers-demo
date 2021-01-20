package com.antoszj.testcontainers.demo.repository;

import com.antoszj.testcontainers.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}