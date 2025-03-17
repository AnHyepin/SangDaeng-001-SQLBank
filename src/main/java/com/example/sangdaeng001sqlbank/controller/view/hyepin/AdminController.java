package com.example.sangdaeng001sqlbank.controller.view.hyepin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/admin")
public class AdminController {


    @GetMapping
    public String admin(){

        return "hyepin/admin/admin-main";
    }
}
