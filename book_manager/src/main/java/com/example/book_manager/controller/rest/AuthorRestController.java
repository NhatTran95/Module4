package com.example.book_manager.controller.rest;

import com.example.book_manager.repository.IAuthorRepository;
import com.example.book_manager.service.response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/authors")
@AllArgsConstructor
public class AuthorRestController {
    private final IAuthorRepository authorRepository;

    @GetMapping
    public List<SelectOptionResponse> getSelectOption(){
        return authorRepository.findAll()
                .stream()
                .map(author -> new SelectOptionResponse(author.getId().toString(), author.getName()))
                .collect(Collectors.toList());
    }
}
