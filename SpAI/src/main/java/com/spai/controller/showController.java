package com.spai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class showController {
    @GetMapping("/chat")
    public String chatpage(){
        return "chat";
    }

    @GetMapping("/")
    public String homepage(){
        return "main/home";
    }

}
