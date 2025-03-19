package com.example.sangdaeng001sqlbank.controller.view.sangin;

import com.example.sangdaeng001sqlbank.utils.SecurityUtil;
import org.hibernate.metamodel.internal.AbstractDynamicMapInstantiator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/common")
public class CommonViewController_sangin {

    @GetMapping("/problemList")
    public String problemList(Model model) {
        String role = SecurityUtil.getRole();
        model.addAttribute("role", role);
        return "sangin/common/problemList";
    }

    @GetMapping("/problemDetail/{problemId}")
    public String problemDetail(@PathVariable("problemId") int problemId, Model model) {
        int userId = SecurityUtil.getUserId();
        String role = SecurityUtil.getRole();
        model.addAttribute("userId", userId);
        model.addAttribute("role", role);
        model.addAttribute("problemId", problemId);
        return "sangin/common/problemDetail";
    }


}
