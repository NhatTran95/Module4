package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller

public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/categories")
    public String showListPage(Model model){
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/category/list";
    }
    @GetMapping("/categories/create")
    public String showCreatePage(Model model){

        model.addAttribute("success", false);
        model.addAttribute("message", "");

        return "/category/create";
    }
    @PostMapping("/categories/create")
    public String create(@ModelAttribute Category category, Model model) {

        categoryService.save(category);

        model.addAttribute("success", true);
        model.addAttribute("message", "Thêm sản phẩm thành công");

        return "category/create";
    }

}
