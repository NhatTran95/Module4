package com.example.book_manager.service.authorService;

import com.example.book_manager.repository.IAuthorRepository;
import com.example.book_manager.service.response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {

    private final IAuthorRepository authorRepository;

    public List<SelectOptionResponse> findAll(){
        return authorRepository.findAll().stream()
                .map(author -> new SelectOptionResponse(author.getId().toString(), author.getName()))
                .collect(Collectors.toList());
    }
}
