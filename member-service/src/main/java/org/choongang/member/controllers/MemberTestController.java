package org.choongang.member.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberTestController {
    @GetMapping("/alive")
    public String testAlive() {
        return "alive";
    }
}
