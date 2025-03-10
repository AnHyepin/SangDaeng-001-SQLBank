package com.example.sangdaeng001sqlbank.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/teacher")
public class TeacherViewController_sangin {
    @GetMapping("/problemList")
    public String problemList() {
        return "sangin/teacher/problemList";
    }

    @GetMapping("/problemDetail/{problemId}")
    public String problemDetail(@PathVariable("problemId") int problemId, Model model) {
        model.addAttribute("problemId", problemId);
        return "sangin/teacher/problemDetail"; // Thymeleaf 템플릿 경로
    }

    @GetMapping("/problemRegist")
    public String problemRegist() {
        return "sangin/teacher/problemRegist";
    }

    @GetMapping("/problemUpdate")
    public String problemUpdate() {
        return "sangin/teacher/problemUpdate";
    }
}
