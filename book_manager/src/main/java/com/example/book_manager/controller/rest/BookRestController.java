package com.example.book_manager.controller.rest;


import com.example.book_manager.service.bookService.BookService;

import com.example.book_manager.service.bookService.request.BookSaveRequest;
import com.example.book_manager.service.bookService.response.BookDetailResponse;
import com.example.book_manager.service.bookService.response.BookListResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@AllArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookListResponse>> getList(@PageableDefault(size = 5) Pageable pageable,
                                                          @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(bookService.getAll(pageable, search), HttpStatus.OK);
    }

    @PostMapping
    public void create(@RequestBody BookSaveRequest request){
        bookService.create(request);
    }

    @GetMapping("{id}")
    public ResponseEntity<BookDetailResponse> findById(@PathVariable Long id){
//        return new ResponseEntity<>(bookService.findById(id), HttpStatus.OK);
        return ResponseEntity.ok().body(bookService.findById(id));
    }

    @PutMapping("{id}")
    public void edit(@RequestBody BookSaveRequest request, @PathVariable Long id) {
        bookService.edit(request, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Boolean isDelete = bookService.delete(id);
        if (isDelete) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
