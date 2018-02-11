package com.lxx.mydemo.nettydemo.provider;

import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.JsonBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lixiaoxiong
 * @version 2018-02-11
 */
@RequestMapping("/demo/test")
@Controller
public class TestController {

    @RequestMapping("/web")
    @JsonBody
    public String test() {
        return "succ";
    }
}
