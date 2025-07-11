package com.gymmanagement.gymapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String mostrarLandingPage() {
        return "landing"; // Esto carga landing.html desde templates
    }

}
