package org.zerock.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController    // 기본 json으로 처리한다.
public class SampleController {
    @GetMapping("/hello")
    public String[] hello(){
        return new String[]{"Hello", "World"};
    }
}
