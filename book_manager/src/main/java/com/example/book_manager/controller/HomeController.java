package com.example.book_manager.controller;


import com.example.book_manager.service.authorService.AuthorService;

import com.example.book_manager.service.categoryService.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {

    private final CategoryService categoryService;

    private final AuthorService authorService;

    @GetMapping
    public ModelAndView home(){
        ModelAndView view = new ModelAndView("index");
        view.addObject("categories", categoryService.findAll());
        view.addObject("authors", authorService.findAll());
        return view;
    }
}
