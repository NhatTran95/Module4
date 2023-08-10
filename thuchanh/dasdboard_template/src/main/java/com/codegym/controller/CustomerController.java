package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.service.customer.CustomerServiceImpl;
import com.codegym.service.customer.ICustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {
    private final ICustomerService customerService = new CustomerServiceImpl();
    @GetMapping("/customers")
    public String showListCustomers(Model model){
        List<Customer> customerList = customerService.findAll();
        model.addAttribute("customers", customerList);
        return "customer/list";
    }
    @GetMapping("/customers/create")
    public String createCustomer(Model model){
        model.addAttribute("customer", new Customer());
        return "customer/create";
    }
    @PostMapping("/customers/save")
    public String save(Customer customer, RedirectAttributes redirect) {
        customerService.save(customer);
        redirect.addFlashAttribute("success", "added customer successfully!");
        return "redirect:/customer";
    }
    @GetMapping("/customers/{id}/edit")
    public String update(@PathVariable long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "customer/update";
    }
    @PostMapping("/customers/update")
    public String update(Customer customer, RedirectAttributes redirect) {
        customerService.save(customer);
        redirect.addFlashAttribute("success", "updated customer successfully!");
        return "redirect:/customer";
    }
    @GetMapping("/customers/{id}/delete")
    public String delete(@PathVariable long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "customer/delete";
    }
    @PostMapping("/customers/delete")
    public String delete(Customer customer, RedirectAttributes redirect) {
        customerService.delete(customer);
        redirect.addFlashAttribute("success", "Removed customer successfully!");
        return "redirect:/customer";
    }
}
