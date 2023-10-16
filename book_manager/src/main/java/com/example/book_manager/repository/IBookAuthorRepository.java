package com.example.book_manager.repository;

import com.example.book_manager.domain.BookAuthor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    @Transactional
    void deleteBookAuthorsByBookId(Long id);
}
