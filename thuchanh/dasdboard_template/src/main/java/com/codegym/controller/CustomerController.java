package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.service.CustomerService;
import com.codegym.service.ICustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {
    private final ICustomerService customerService = new CustomerService();
    @GetMapping("/customer")
    public String showListCustomers(Model model){
        List<Customer> customerList = customerService.findAll();
        model.addAttribute("customers", customerList);
        return "customer/list";
    }
    @GetMapping("/customer/create")
    public String createCustomer(Model model){
        model.addAttribute("customer", new Customer());
        return "customer/create";
    }
    @PostMapping("/customer/save")
    public String save(Customer customer, RedirectAttributes redirect) {
        customer.setId((int) (Math.random() * 10000));
        customerService.save(customer);
        redirect.addFlashAttribute("success", "added customer successfully!");
        return "redirect:/customer";
    }
    @GetMapping("/customer/{id}/edit")
    public String update(@PathVariable int id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "customer/update";
    }
    @PostMapping("/customer/update")
    public String update(Customer customer, RedirectAttributes redirect) {
        customerService.update(customer.getId(), customer);
        redirect.addFlashAttribute("success", "updated customer successfully!");
        return "redirect:/customer";
    }
    @GetMapping("/customer/{id}/delete")
    public String delete(@PathVariable int id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "customer/delete";
    }
    @PostMapping("/customer/delete")
    public String delete(Customer customer, RedirectAttributes redirect) {
        customerService.remove(customer.getId());
        redirect.addFlashAttribute("success", "Removed customer successfully!");
        return "redirect:/customer";
    }
}
