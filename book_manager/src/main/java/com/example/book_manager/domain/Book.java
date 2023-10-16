package com.example.book_manager.domain;

import com.example.book_manager.domain.enums.EType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = 0")
@SQLDelete(sql = "UPDATE books SET `deleted` = 1 WHERE (`id` = ?);")

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDate publishDate;

    private boolean deleted = false;

    @Column(precision = 12, scale = 0)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private EType type;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> bookAuthors;

    @OneToMany(mappedBy = "book")
    private List<BookImage> images;
}
