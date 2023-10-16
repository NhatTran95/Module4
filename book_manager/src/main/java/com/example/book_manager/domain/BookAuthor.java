package com.example.book_manager.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "book_authors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = 0")
@SQLDelete(sql = "UPDATE book_authors SET `deleted` = 1 WHERE (`id` = ?);")
public class BookAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean deleted = false;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Author author;

    public BookAuthor(Book book, Author author){
        this.book = book;
        this.author = author;
    }
}
