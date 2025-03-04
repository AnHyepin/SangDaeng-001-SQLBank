package com.example.sangdaeng001sqlbank.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/teacher")
public class TeacherViewController_sangin {
    @GetMapping("/problemList")
    public String problemList() {
        return "sangin/teacher/problemList";
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
