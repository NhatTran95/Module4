package com.example.book_manager.repository;

import com.example.book_manager.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorRepository extends JpaRepository<Author, Long> {
}
