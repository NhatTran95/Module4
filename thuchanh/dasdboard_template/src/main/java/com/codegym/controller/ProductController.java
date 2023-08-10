package com.codegym.controller;


import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/products")
public class ProductController {
//    private final IProductService productService = new ProductServiceImpl();

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

   @GetMapping("")
    public String showListPage(Model model){
       List<Product> products = productService.findAll();
       model.addAttribute("products", products);
       return "/product/list";
   }
   @GetMapping("/create")
    public String showCreatePage(Model model){

       List<Category> categories = categoryService.findAll();

       model.addAttribute("success", false);
       model.addAttribute("message", "");
       model.addAttribute("categories", categories);

       return "/product/create";
   }
    @GetMapping("/edit/{productId}")
    public String showUpdatePage(@PathVariable Long productId, Model model) {

        Optional<Product> productOptional = productService.findById(productId);
        List<Category> categories = categoryService.findAll();

        if (!productOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
        }
        else {
            model.addAttribute("error", false);
            model.addAttribute("message", "");
            model.addAttribute("categories", categories);

            model.addAttribute("product", productOptional.get());
        }

        return "product/update";
    }

    @GetMapping("/delete/{productId}")
    public String showDeletePage(@PathVariable Long productId, Model model) {

        Optional<Product> productOptional = productService.findById(productId);

        if (!productOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
        }
        else {
            model.addAttribute("error", false);
            model.addAttribute("message", "");

            model.addAttribute("product", productOptional.get());
        }

        return "product/delete";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Product product, Model model) {

        productService.save(product);

        model.addAttribute("success", true);
        model.addAttribute("message", "Thêm sản phẩm thành công");

        return "product/create";
    }

    @PostMapping("/edit/{productId}")
    public String update(@PathVariable Long productId, @ModelAttribute Product product, Model model) {
        Optional<Product> productOptional = productService.findById(productId);

        if (!productOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
            model.addAttribute("product", product);
            return "product/update";
        }
        else {
            product.setId(productId);
            productService.save(product);
            model.addAttribute("message", "Đã sửa thành công ");
        }
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/product/list";
    }
    @PostMapping("/delete/{productId}")
    public String delete(@PathVariable Long productId, @ModelAttribute Product product, Model model) {
        Optional<Product> productOptional = productService.findById(productId);

        if (!productOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Sản phẩm không tồn tại");
            model.addAttribute("product", product);
            return "product/delete";
        }
        else {
            product.setId(productId);
            productService.delete(product);
            model.addAttribute("message", "Đã xóa thành công ");
        }
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/product/list";
    }

}
