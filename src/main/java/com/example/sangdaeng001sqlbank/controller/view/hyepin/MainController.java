package com.example.sangdaeng001sqlbank.controller.view.hyepin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login(){

        return "hyepin/common/login";
    }

    @GetMapping("/join")
    public String join(){

        return "hyepin/common/login";
    }

}
