package ru.maxima.springrest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//1 @Controller - RestController заменяет поведение 1
@RestController
@RequestMapping( "/api/v1")
public class HelloRestController {

//1    @ResponseBody
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello, world!";
    }

//1    @ResponseBody
    @GetMapping("/sayGoodbye")
    public String sayGoodbye() {
        return "Goodbye, world!";
    }
}
