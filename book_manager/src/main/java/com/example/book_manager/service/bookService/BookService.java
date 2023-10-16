package com.example.book_manager.service.bookService;

import com.example.book_manager.domain.*;
import com.example.book_manager.repository.IBookAuthorRepository;
import com.example.book_manager.repository.IBookImageRepossitory;
import com.example.book_manager.repository.IBookRepository;
import com.example.book_manager.service.bookService.request.BookSaveRequest;
import com.example.book_manager.service.bookService.response.BookDetailResponse;
import com.example.book_manager.service.bookService.response.BookListResponse;
import com.example.book_manager.service.request.SelectOptionRequest;
import com.example.book_manager.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {

    private final IBookRepository bookRepository;

    private final IBookAuthorRepository bookAuthorRepository;

    private final IBookImageRepossitory fileRepository;

    public Page<BookListResponse> getAll(Pageable pageable, String search) {
        search = "%" + search + "%";

        return bookRepository.search(pageable, search).map(book -> {
            var result = AppUtil.mapper.map(book, BookListResponse.class);
            result.setCategory(book.getCategory().getName());
            result.setAuthors(book.getBookAuthors().stream()
                    .map(bookAuthor -> bookAuthor.getAuthor().getName())
                    .collect(Collectors.joining(",")));
            result.setImages(book.getImages()
                    .stream()
                    .map(BookImage::getFileUrl)
                    .collect(Collectors.toList()));
            return result;
        });
    }

    public void create(BookSaveRequest request) {
        var book = AppUtil.mapper.map(request, Book.class);
        book = bookRepository.save(book);

        Book finalBook = book;
        bookAuthorRepository.saveAll(request.getIdAuthors()
                .stream()
                .map(id -> new BookAuthor(finalBook, new Author(Long.valueOf(id))))
                .collect(Collectors.toList()));
        var files = fileRepository.findAllById(request.getFiles()
                .stream().map(SelectOptionRequest::getId)
                .collect(Collectors.toList()));
        for (var file : files){
            file.setBook(book);
        }
        fileRepository.saveAll(files);

    }

    public BookDetailResponse findById(Long id) {
        var book = bookRepository.findById(id).orElse(new Book());
        var result = AppUtil.mapper.map(book, BookDetailResponse.class);
        result.setIdCategory(book.getCategory().getId());
        result.setIdAuthors(book.getBookAuthors()
                .stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getId())
                .collect(Collectors.toList()));
        List<String> images = book.getImages().stream()
                .map(BookImage::getFileUrl)
                .collect(Collectors.toList());
        result.setImages(images);
        return result;

    }

    public void edit(BookSaveRequest request, Long id) {
        var book = bookRepository.findById(id).orElse(new Book());
        book.setCategory(new Category());
        AppUtil.mapper.map(request, book);

        bookAuthorRepository.deleteAll(book.getBookAuthors());
        var bookAuthors = new ArrayList<BookAuthor>();
        for (String idAuthor : request.getIdAuthors()) {
            Author author = new Author(Long.valueOf(idAuthor));
            bookAuthors.add(new BookAuthor(book, author));
        }
        bookAuthorRepository.saveAll(bookAuthors);
        book.setBookAuthors(bookAuthors);
        book = bookRepository.save(book);

//        for(BookImage image: book.getImages()){
//            fileRepository.delete(image);
//        }
        var files = fileRepository.findAllById(request.getFiles().stream()
                .map(SelectOptionRequest::getId)
                .collect(Collectors.toList()));
        for (var file : files){
            file.setBook(book);
        }
        fileRepository.saveAll(files);
    }

    public Boolean delete(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if(bookOptional.isPresent()){
            bookAuthorRepository.deleteBookAuthorsByBookId(id);

            for (var image : bookOptional.get().getImages()){
                fileRepository.delete(image);
            }

            bookRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
