package com.h3c.isearch.controller;

import com.h3c.isearch.service.ISearchRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-07-03 10:52
 **/
@Controller
@RequestMapping("/searchRange")
public class SearchRangeController {

    @Autowired
    ISearchRangeService searchRangeService;

    @GetMapping("/getRangeView")
    public String getRangeView(){
        return "view/searchRangeView";
    }

    @PostMapping("/searchRangeTable")
    @ResponseBody
    public List<Map<String,Object>> searchRangeTable(){
        List<Map<String,Object>> rangeTable = searchRangeService.queryRangeTable();
        if(rangeTable == null){
            rangeTable = new ArrayList<>();
        }
        return rangeTable;
    }

}


