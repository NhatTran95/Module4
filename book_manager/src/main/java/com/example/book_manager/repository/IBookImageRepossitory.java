package com.example.book_manager.repository;

import com.example.book_manager.domain.BookImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookImageRepossitory extends JpaRepository<BookImage, String> {
    @Transactional
    void deleteBookImageByFileUrl(String fileUrl);
}
