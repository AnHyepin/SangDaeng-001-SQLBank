package com.example.sangdaeng001sqlbank.controller.view.hyepin;

import com.example.sangdaeng001sqlbank.service.hyepin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String admin(Model model) {
        int classNum = adminService.getClassNumber();
        model.addAttribute("classNum", classNum);
        return "hyepin/admin/admin-main";
    }

}
