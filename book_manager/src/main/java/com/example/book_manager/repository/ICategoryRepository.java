package com.example.book_manager.repository;

import com.example.book_manager.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
}
