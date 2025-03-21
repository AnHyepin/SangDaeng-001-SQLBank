package com.example.sangdaeng001sqlbank.controller.view.sangin;

import com.example.sangdaeng001sqlbank.utils.SecurityUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String postList(@RequestParam("category") String category,
                           Model model) {
        int userId = SecurityUtil.getUserId();
        String role = SecurityUtil.getRole();

        String categoryText = switch (category) {
            case "N" -> "공지사항";
            case "F" -> "자유게시판";
            case "S" -> "건의사항";
            case "Q" -> "Q&A";
            default -> "게시판";
        };

        model.addAttribute("userId", userId);
        model.addAttribute("role", role);
        model.addAttribute("category", category);
        model.addAttribute("categoryText", categoryText);

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
