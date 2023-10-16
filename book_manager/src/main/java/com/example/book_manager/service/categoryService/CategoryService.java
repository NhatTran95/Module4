package com.example.book_manager.service.categoryService;

import com.example.book_manager.repository.ICategoryRepository;
import com.example.book_manager.service.response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final ICategoryRepository categoryRepository;

    public List<SelectOptionResponse> findAll(){
        return categoryRepository.findAll().stream()
                .map(category -> new SelectOptionResponse(category.getId().toString(), category.getName()))
                .collect(Collectors.toList());
    }
}
