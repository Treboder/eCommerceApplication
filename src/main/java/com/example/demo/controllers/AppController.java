package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    private Logger log = LoggerFactory.getLogger(CartController.class);

    @GetMapping("/hello")
    public String Hello(){
        log.info("Hello my friend");
        return "<h1>Hello my friend</h1>";
    }

}
