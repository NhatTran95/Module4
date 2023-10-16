package com.example.book_manager.service.bookService.response;

import com.example.book_manager.domain.enums.EType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDate publishDate;

    private BigDecimal price;

    private EType type;

    private Long idCategory;

    private List<Long> idAuthors;

    private List<String> images;
}
