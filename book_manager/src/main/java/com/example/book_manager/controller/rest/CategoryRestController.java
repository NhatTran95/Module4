package com.example.book_manager.controller.rest;

import com.example.book_manager.repository.ICategoryRepository;
import com.example.book_manager.service.response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/categories")
@AllArgsConstructor
public class CategoryRestController {
    private final ICategoryRepository categoryRepository;


    @GetMapping
    public List<SelectOptionResponse> getSelectOption(){
        return categoryRepository.findAll().stream()
                .map(category -> new SelectOptionResponse(category.getId().toString(), category.getName()))
                .collect(Collectors.toList());
    }
}
