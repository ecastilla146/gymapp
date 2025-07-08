// src/main/java/com/gymmanagement/gymapp/controller/ClientController.java

package com.gymmanagement.gymapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {

    @GetMapping("/dashboard")
    public String clientDashboard() {
        return "client/dashboard"; // Asume que tienes un archivo Thymeleaf en src/main/resources/templates/client/dashboard.html
    }
}