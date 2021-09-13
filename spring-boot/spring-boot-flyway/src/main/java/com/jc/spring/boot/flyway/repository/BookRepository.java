package com.jc.spring.boot.flyway.repository;

import com.jc.spring.boot.flyway.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer>{
}
