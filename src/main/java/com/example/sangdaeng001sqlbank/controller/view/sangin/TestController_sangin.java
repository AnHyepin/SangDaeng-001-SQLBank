package com.example.sangdaeng001sqlbank.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController_sangin {
    @GetMapping("/")
    public String test1() {
        return "sangin/main";
    }
}
