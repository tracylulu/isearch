package com.h3c.isearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: isearch-base
 * @description:
 * @author: zhanghao
 * @create: 2020-05-20 14:37
 **/

@Controller
@RequestMapping("/globalQuery")
public class GlobalQueryController {

    @GetMapping("/returnQueryView")
    public String returnQueryView(){
        return "view/globalQueryView";
    }

}
