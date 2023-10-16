package com.example.book_manager.service.bookService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDate publishDate;

    private BigDecimal price;

    private String type;

    private String category;

    private String authors;

    private List<String> images;
}
