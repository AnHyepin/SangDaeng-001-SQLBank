package com.example.sangdaeng001sqlbank.controller.view.sangin;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/student")
public class StudentViewController_sangin {

    @GetMapping("/difficulty")
    public String problemList() {
        return "sangin/student/difficulty";
    }

    @GetMapping("/exam")
    public String problemSolve() {
        return "sangin/student/exam";
    }

    @GetMapping("/scoreList")
    public String scoreList() {
        return "sangin/student/scoreList";
    }
}
