package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.model.Product;
import com.codegym.service.IProductService;
import com.codegym.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class ProductController {
    private final IProductService productService = new ProductService();

   @GetMapping("/product")
    public String showAllProduct(Model model){
       List<Product> products = productService.findAll();
       model.addAttribute("products", products);
       return "/product/list";
   }
   @GetMapping("/product/create")
    public String createProduct(Model model){
       model.addAttribute("product", new Product());
       return "/product/create";
   }
   @PostMapping("/product/save")
    public String save(Product product, RedirectAttributes redirect){
       product.setId((int) (Math.random() * 10000));
       productService.save(product);
       redirect.addFlashAttribute("success", "added product successfully!");
       return "redirect:/product";
   }
    @GetMapping("/product/{id}/edit")
    public String update(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "product/update";
    }
    @PostMapping("/product/update")
    public String update(Product product, RedirectAttributes redirect) {
        productService.update(product.getId(), product);
        redirect.addFlashAttribute("success", "updated product successfully!");
        return "redirect:/product";
    }
    @GetMapping("/product/{id}/delete")
    public String delete(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "product/delete";
    }
    @PostMapping("/product/delete")
    public String delete(Product product, RedirectAttributes redirect) {
        productService.remove(product.getId());
        redirect.addFlashAttribute("success", "Removed customer successfully!");
        return "redirect:/product";
    }
}
