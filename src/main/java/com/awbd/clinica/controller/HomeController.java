package com.awbd.clinica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
        @RequestMapping({"", "/", "/index"})
        public String home() {
            return "index";
        }

        @GetMapping("/login")
        public String login() {
            return "login";
        }

        @GetMapping("/access_denied")
        public String accessDenied() {
            return "accessDenied";
        }
    }

