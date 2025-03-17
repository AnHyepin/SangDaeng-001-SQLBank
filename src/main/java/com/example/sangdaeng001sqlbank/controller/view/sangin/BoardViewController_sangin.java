package com.example.sangdaeng001sqlbank.controller.view.sangin;

import com.example.sangdaeng001sqlbank.utils.SecurityUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/common")
public class BoardViewController_sangin {
    @GetMapping("/postDetail/{postId}")
    public String postDetail(@PathVariable int postId, Model model) {
        int userId = SecurityUtil.getUserId();
        String role = SecurityUtil.getRole();
        model.addAttribute("userId", userId);
        model.addAttribute("role", role);
        model.addAttribute("postId", postId);
        return "sangin/common/postDetail";
    }

    @GetMapping("/postList")
    public String postList(@Param("category") String category, Model model) {
        String categoryText = null;
        switch (category) {
            case "N":
                categoryText = "공지사항";
                break;
            case "F":
                categoryText = "자유게시판";
                break;
            case "S":
                categoryText = "건의사항";
                break;
            case "Q":
                categoryText = "Q&A";
                break;
        }
        int userId = SecurityUtil.getUserId();
        String role = SecurityUtil.getRole();
        model.addAttribute("userId", userId);
        model.addAttribute("role", role);
        model.addAttribute("categoryText", categoryText);
        model.addAttribute("category", category);
        return "sangin/common/postList";
    }

    @GetMapping("/postRegist")
    public String postRegist() {
        return "sangin/common/postRegist";
    }

    @GetMapping("/postUpdate/{postId}")
    public String postUpdate(@PathVariable("postId") int postId, Model model) {
        model.addAttribute("postId", postId);
        return "sangin/common/postUpdate";
    }
}
