package com.example.book_manager.repository;

import com.example.book_manager.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IBookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT b FROM Book b " +
            "WHERE " +
            "b.title like :search OR " +
            "b.description like :search or " +
            "b.category.name like :search or " +
            "exists (select 1 from BookAuthor ba where ba.book = b and ba.author.name like :search)")
    Page<Book> search(Pageable pageable, String search);
}
