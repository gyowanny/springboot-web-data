package com.example.springbootwebdata.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/private")
public class StatusController {

    @RequestMapping(path="/status", method = GET)
    public String status() {
        return "OK";
    }
}
