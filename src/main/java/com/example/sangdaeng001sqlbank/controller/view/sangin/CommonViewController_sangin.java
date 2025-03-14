package com.example.sangdaeng001sqlbank.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/common")
public class CommonViewController_sangin {

    @GetMapping("/problemList")
    public String problemList() {
        return "sangin/common/problemList";
    }

    @GetMapping("/problemDetail/{problemId}")
    public String problemDetail(@PathVariable("problemId") int problemId, Model model) {
        model.addAttribute("problemId", problemId);
        return "sangin/common/problemDetail";
    }


}
