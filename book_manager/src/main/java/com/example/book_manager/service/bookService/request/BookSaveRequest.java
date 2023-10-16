package com.example.book_manager.service.bookService.request;

import com.example.book_manager.service.request.SelectOptionRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookSaveRequest {
    private String title;

    private String description;

    private String publishDate;

    private String price;

    private String type;

    private List<String> idAuthors;

    private SelectOptionRequest category;

    private List<SelectOptionRequest> files;
}
