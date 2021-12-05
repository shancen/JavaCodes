package com.example.demo.controller;

import com.example.demo.autoconfiguration.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource(name = "student")
    private StudentService studentService;

    @GetMapping("/say")
    public String sayWhat() {
        return studentService.getId() + studentService.getName();
    }
}
