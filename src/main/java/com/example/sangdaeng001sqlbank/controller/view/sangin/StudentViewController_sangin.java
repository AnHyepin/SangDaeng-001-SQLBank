package com.example.sangdaeng001sqlbank.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/student")
public class StudentViewController_sangin {

    @GetMapping("/problemList")
    public String problemList() {
        return "sangin/student/problemList";
    }

    @GetMapping("/problemSolve")
    public String problemSolve() {
        return "sangin/student/problemSolve";
    }

    @GetMapping("/scoreList")
    public String scoreList() {
        return "sangin/student/scoreList";
    }
}
