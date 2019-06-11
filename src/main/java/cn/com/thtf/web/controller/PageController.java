package cn.com.thtf.web.controller;

import cn.com.thtf.common.response.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/10
 * Time：8:52
 * Version: v1.0
 * ========================
 */
@Controller
@RequestMapping("/v1")
public class PageController {

    @GetMapping("/log")
    public String showLog() {
        return "logs/showLog";
    }
}
