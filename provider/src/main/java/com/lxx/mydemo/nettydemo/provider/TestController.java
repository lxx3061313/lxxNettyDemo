package com.lxx.mydemo.nettydemo.provider;

import com.lxx.mydemo.nettydemo.service.common.web.spring.annotation.JsonBody;
import com.lxx.mydemo.nettydemo.service.impl.TerminalQueryService;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lixiaoxiong
 * @version 2018-02-11
 */
@RequestMapping("/netty/demo/test")
@Controller
public class TestController {

    @Resource
    TerminalQueryService terminalQueryService;

    @RequestMapping("/queryTerminalParam")
    @JsonBody
    public void test(String id) {
        terminalQueryService.queryTerminalParam(id);
    }

    @RequestMapping("/exactParam")
    @JsonBody
    public void queryExactParam(String tid, String ids) {
        terminalQueryService.queryExactTerminalParam(tid, ids);
    }

    @RequestMapping("/attr")
    @JsonBody
    public void queryTerminalAttr(String id) {
        terminalQueryService.queryTerminalAttri(id);
    }
}
