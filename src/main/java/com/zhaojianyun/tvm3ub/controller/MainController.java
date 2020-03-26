package com.zhaojianyun.tvm3ub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhaojianyun
 */
@Controller
@RequestMapping("/main")
public class MainController {

    @RequestMapping("/")
    public String loadPlaylist() {
        return "";
    }
}
