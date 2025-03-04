package com.example.sangdaeng001sqlbank.controller.view.hyepin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {


    @GetMapping("/admin")
    public String admin(){

        return "hyepin/admin/admin-test";
    }
}
