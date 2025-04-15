package io.github.artemnefedov.rstech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping
    public String index() {
        return "index.html";
    }

    @GetMapping("/categories")
    public String categories() {
        return "categories.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
}
