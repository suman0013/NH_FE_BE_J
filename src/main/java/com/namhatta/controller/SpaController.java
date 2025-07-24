package com.namhatta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {
    
    // Forward SPA routes to React's index.html
    @GetMapping(value = {"/dashboard", "/namhattas", "/devotees", "/hierarchy", "/more", "/login"})
    public String forward() {
        return "forward:/index.html";
    }
}