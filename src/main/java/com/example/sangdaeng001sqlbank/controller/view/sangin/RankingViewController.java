package com.example.sangdaeng001sqlbank.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/common")
public class RankingViewController {
    @GetMapping("/ranking")
    public String ranking() {
        return "sangin/common/ranking";
    }
}
