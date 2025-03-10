package com.example.sangdaeng001sqlbank.controller.view.hyepin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/common")
@Slf4j
public class LoinController {

    @GetMapping("/login")
    public String login(){
        log.info("로그인 컨트롤러");
        return "hyepin/common/login";
    }

    @GetMapping("/join")
    public String join(){
        return "hyepin/common/join";
    }


}
