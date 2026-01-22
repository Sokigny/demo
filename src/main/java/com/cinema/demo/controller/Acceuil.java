package com.cinema.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Acceuil {
    
    @GetMapping("/")
    public String home() {
        return "hello";
    }
}
