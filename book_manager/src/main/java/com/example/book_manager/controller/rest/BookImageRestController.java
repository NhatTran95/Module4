package com.example.book_manager.controller.rest;

import com.example.book_manager.domain.BookImage;
import com.example.book_manager.service.bookImageService.BookImageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/bookImages")
@AllArgsConstructor
public class BookImageRestController {
    private final BookImageService bookImageService;

    @PostMapping
    public BookImage upload(@RequestParam("avatar")MultipartFile avatar) throws IOException {
        return bookImageService.saveAvatar(avatar);
    }

    @DeleteMapping()
    public void delete(@RequestParam("url") String url) {
        bookImageService.delete(url);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable String id) {
        bookImageService.deleteById(id);
    }

}
