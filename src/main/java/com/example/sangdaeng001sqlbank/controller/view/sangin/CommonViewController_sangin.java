package com.example.sangdaeng001sqlbank.controller.view.sangin;

import com.example.sangdaeng001sqlbank.utils.SecurityUtil;
import org.hibernate.metamodel.internal.AbstractDynamicMapInstantiator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view/common")
public class CommonViewController_sangin {

    @Controller
    @RequestMapping("/view/common")
    public class ProblemViewController {

        @GetMapping("/problemList")
        public String problemList(
                @RequestParam(value = "page", defaultValue = "1") int page,
                @RequestParam(value = "size", defaultValue = "20") int size,
                Model model) {

            int userId = SecurityUtil.getUserId();
            String role = SecurityUtil.getRole();

            model.addAttribute("userId", userId);
            model.addAttribute("role", role);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);

            return "sangin/common/problemList";
        }
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
