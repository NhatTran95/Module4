package com.codegym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping("")
    public String index(){
        return "/index";
    }
    @GetMapping("/infor")
    public String infor(){
        return "/infor";
    }
    @GetMapping("/customers")
    public String customer(){
        return "/customers";
    }
    @GetMapping("/tours")
    public String tour(){
        return "/tours";
    }
    @GetMapping("/revenue")
    public String revenue(){
        return "/revenue";
    }
    @GetMapping("/logout")
    public String logout(){
        return "/logout";
    }

}
