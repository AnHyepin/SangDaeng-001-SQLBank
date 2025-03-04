package com.example.sangdaeng001sqlbank.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/common")
public class CommonViewController_sangin {

    @GetMapping("/communityDetail")
    public String communityDetail() {
        return "sangin/common/communityDetail";
    }

    @GetMapping("/communityList")
    public String communityList() {
        return "sangin/common/communityList";
    }

    @GetMapping("/suggestionDetail")
    public String suggestionDetail() {
        return "sangin/common/suggestionDetail";
    }

    @GetMapping("/suggestionList")
    public String suggestionList() {
        return "sangin/common/suggestionList";
    }
}
