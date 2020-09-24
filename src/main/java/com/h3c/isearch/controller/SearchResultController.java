package com.h3c.isearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.h3c.isearch.mapper.ClickLogMapper;
import com.h3c.isearch.mapper.SearchLogMapper;
import com.h3c.isearch.service.IClickLogService;
import com.h3c.isearch.service.ISearchLogService;
import com.h3c.isearch.util.Constant;
import com.h3c.isearch.util.HttpUtil;
import com.h3c.isearch.util.ReqUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: isearch-base
 * @description:
 * @author: zhanghao
 * @create: 2020-05-25 17:26
 **/

@Controller
@RequestMapping("/searchResult")
public class SearchResultController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ISearchLogService searchLogService;

    @Autowired
    IClickLogService clickLogService;

    @PostMapping("/getSearchResultView")
    public String getSearchResultView(HttpServletRequest request, String searchParam, HashMap<String,Object> model){
        String logSearchId = null;
        String title = "-";
        String resultStr = null;
        Date reqDate = new Date();
        String searchJsonStr = null;
        boolean searchFlag = false;
        long total = 0l;
        List<Map<String,Object>> results = new ArrayList<>();
        if(StringUtils.isNotBlank(searchParam)){
            title = searchParam;
        }
        if(searchParam != null && StringUtils.isNotBlank(Constant.esUrl)){
            try{
                String searchKey = searchParam;
                if("*".equals(searchKey)){
                    searchKey = "";
                }
                searchJsonStr = "{\"query\": { \"bool\": { \"must\": [";
                if(StringUtils.isBlank(searchKey)){
                    searchJsonStr += "] } },";
                }else{
                    searchJsonStr += "{ \"match_phrase\": { \"attachment.content\": \""+searchKey+"\" } }] } },";
                }
                searchJsonStr +="\"_source\": [\"filedir\",\"filename\",\"filetype\",\"filesize\",\"datasource\"],"
                        +"\"from\": 0,"
                        +"\"size\": 50,"
                        +"\"highlight\": { \"fields\": { \"attachment.content\": {\"fragment_size\":70, \"number_of_fragments\" : 3} } }}";
                //Map<String,Object> param =  JSONObject.parseObject(searchJsonStr);
                Map<String,Object> httpResultMap = HttpUtil.sendPost(Constant.esUrl,searchJsonStr);
                if(httpResultMap != null){
                    resultStr = JSONObject.toJSONString(httpResultMap);
                    Map<String,Object> hitsMap = (Map<String,Object>)httpResultMap.get("hits");
                    total = ((Integer) hitsMap.get("total")).longValue();
                    results = (List)hitsMap.get("hits");
                    if(results.size()>0){
                        for(Map<String,Object> map :results){
                            String allStr = "";
                            Map<String,Object> highlightMap = (Map)map.get("highlight");
                            if(highlightMap != null){
                                List<String> contentList = (List)highlightMap.get("attachment.content");
                                for(String content:contentList){
                                    if(StringUtils.isNotBlank(content)){
                                        allStr += content.replaceAll("<em>","<span style='color:red;'>").replaceAll("</em>","</span>")+";";
                                    }
                                }
                            }
                            Map<String,Object> sourceMap = (Map)map.get("_source");
                            if(sourceMap != null){
                                String filedir = (String) sourceMap.get("filedir");
                                if(StringUtils.isNotBlank(filedir) && filedir.length()>4 && filedir.substring(0,4).equalsIgnoreCase("http")){
                                    sourceMap.put("filedir","http"+filedir.substring(4));
                                }
                            }

                            if(StringUtils.isNotBlank(allStr)){
                            	if(allStr.length()>300) {
                            		allStr = (allStr.substring(0,300))+"...";
                            	}else {
                            		allStr = (allStr.substring(0,allStr.length()-1))+"...";
                            	}
                                
                            }
                            map.put("allStr",allStr);
                        }
                    }
                }
                searchFlag = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        int resultSize = results.size();
        if(resultSize>0){
            Map<String,Object> pageDataMap = new HashMap<>();
            int resultPageCount;
            if(resultSize % 10 == 0){
                resultPageCount = resultSize/10;
            } else{
                resultPageCount = (resultSize/10)+1;
            }
            for(int i =1;i<(resultPageCount+1);i++){
            	 int start = (i-1)*10;
                 int end = 0;
                 if(resultPageCount == i){
                 	end = results.size();
                 }else {
                 	end = i*10;	
                 }
                pageDataMap.put("page"+i,results.subList(start,end));
            }
            model.put("fivePageData",pageDataMap);
        }

        Date responDate = new Date();
        try {
            Map<String,Object> log = searchLogService.insertLog(request,searchJsonStr,resultStr,reqDate,responDate);
            logSearchId = (String)log.get("searchId");
        }catch (Exception e){
            e.printStackTrace();
        }

        model.put("logSearchId",logSearchId);
        model.put("total",total);
        model.put("results",results);
        model.put("title",title);
        model.put("searchParam",searchParam);
        model.put("searchFlag",searchFlag);
        return "view/searchResult";

    }

    @PostMapping("/searchData")
    @ResponseBody
    public Map<String,Object> searchData(HttpServletRequest request,@RequestParam(value = "searchKey",required = true) String searchKey,int pageNumber,
                                         @RequestParam(value = "searchTime",required = true) String searchTime,
                                         String startTime,String endTime,
                                         String logType,String clickType,String clickSearchId){
        String resultStr = null;
        Date reqDate = new Date();
        String searchJsonStr = null;
        Map<String,Object> resultMap = new HashMap<>();
        long total = 0l;
        List<Map<String,Object>> results = new ArrayList<>();
        boolean reqFlag = false;
        if(searchKey != null && searchTime != null && StringUtils.isNotBlank(Constant.esUrl) && pageNumber>0){
            try {
                if("*".equals(searchKey)){
                    searchKey = "";
                }
                searchJsonStr = "{\"query\": { \"bool\": { \"must\": [";
                if(StringUtils.isNotBlank(searchKey)){
                    searchJsonStr += "{ \"match_phrase\": { \"attachment.content\": \""+searchKey+"\" } },";
                }
                String timeStr = computingTime(searchTime,startTime,endTime);
                if(StringUtils.isNotBlank(timeStr)){
                    searchJsonStr += timeStr;
                }
                if(searchJsonStr.endsWith(",")){
                    searchJsonStr = searchJsonStr.substring(0,searchJsonStr.length()-1);
                }
                searchJsonStr += "] } },";
                int from = 10 * (pageNumber-1);
                searchJsonStr +="\"_source\": [\"filedir\",\"filename\",\"filetype\",\"filesize\",\"datasource\" ],"
                        +"\"from\": "+from+","
                        +"\"size\": 50,"
                        +"\"highlight\": { \"fields\": { \"attachment.content\": {\"fragment_size\":70, \"number_of_fragments\" : 3} } }}";
                Map<String,Object> httpResultMap = HttpUtil.sendPost(Constant.esUrl,searchJsonStr);
                if(httpResultMap != null){
                    resultStr = JSONObject.toJSONString(httpResultMap);
                    Map<String,Object> hitsMap = (Map<String,Object>)httpResultMap.get("hits");
                    total = ((Integer) hitsMap.get("total")).longValue();
                    results = (List)hitsMap.get("hits");
                    if(results.size()>0){
                        for(Map<String,Object> map :results){
                            String allStr = "";
                            Map<String,Object> highlightMap = (Map)map.get("highlight");
                            if(highlightMap != null){
                                List<String> contentList = (List)highlightMap.get("attachment.content");
                                for(String content:contentList){
                                    if(StringUtils.isNotBlank(content)){
                                        allStr += content.replaceAll("<em>","<span style='color:red;'>").replaceAll("</em>","</span>")+";";
                                    }
                                }
                            }
                            if(StringUtils.isNotBlank(allStr)){
                            	if(allStr.length()>300) {
                            		allStr = (allStr.substring(0,300))+"...";
                            	}else {
                            		allStr = (allStr.substring(0,allStr.length()-1))+"...";
                            	}
                            }
                            map.put("allStr",allStr);
                        }
                    }
                }
                reqFlag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Date responDate = new Date();
        try {
            Map<String,Object> log = searchLogService.insertLog(request,searchJsonStr,resultStr,reqDate,responDate);
            if("search".equals(logType)){
                String logSearchId = (String)log.get("searchId");
                resultMap.put("logSearchId",logSearchId);
            }else if("click".equals(logType)){
                int rankNumber = -1;
                String linkURL = null;
                if(StringUtils.isNotBlank(clickSearchId)){
                    clickLogService.insertLog(request,clickType,pageNumber,rankNumber,linkURL,clickSearchId);
                }else{
                    logger.error("记录日志出错：点击日志searchID为空");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        int resultSize = results.size();
        if(resultSize>0){
            Map<String,Object> pageDataMap = new HashMap<>();
            int resultPageCount;
            if(resultSize % 10 == 0){
                resultPageCount = resultSize/10;
            } else{
                resultPageCount = (resultSize/10)+1;
            }
            for(int i =1;i<(resultPageCount+1);i++){
                int start = (i-1)*10;
                int end = 0;
                if(resultPageCount == i){
                	end = results.size();
                }else {
                	end = i*10;	
                }
                pageDataMap.put("page"+(pageNumber+i-1),results.subList(start,end));
            }
            resultMap.put("fivePageData",pageDataMap);
        }

        resultMap.put("reqFlag",reqFlag);
        resultMap.put("reqTotal",total);
        resultMap.put("reqResults",results);
        return resultMap;
    }

    public String computingTime(String timeType,String begin,String end) throws Exception {
        String searchStr = "";
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cld = Calendar.getInstance();
        cld.setTime(new Date());
        if("all".equals(timeType)){
            searchStr = "";
        }else if("24hour".equals(timeType)){
            searchStr = "{ \"range\": { \"attachment.date\": {";
            cld.add(Calendar.DATE,-1);
            String startTime = sdf.format(cld.getTime());
            String endTime = sdf.format(nowDate);
            searchStr += "\"gte\": \""+startTime+"\",\"lte\": \""+endTime+"\"";
            searchStr += "} } }";
        }else if("oneWeek".equals(timeType)){
            searchStr = "{ \"range\": { \"attachment.date\": {";
            cld.add(Calendar.DATE,-7);
            String startTime = sdf.format(cld.getTime());
            String endTime = sdf.format(nowDate);
            searchStr += "\"gte\": \""+startTime+"\",\"lte\": \""+endTime+"\"";
            searchStr += "} } }";
        }else if("oneMonth".equals(timeType)){
            searchStr = "{ \"range\": { \"attachment.date\": {";
            cld.add(Calendar.MONTH,-1);
            String startTime = sdf.format(cld.getTime());
            String endTime = sdf.format(nowDate);
            searchStr += "\"gte\": \""+startTime+"\",\"lte\": \""+endTime+"\"";
            searchStr += "} } }";
        }else if("lastYear".equals(timeType)){
            searchStr = "{ \"range\": { \"attachment.date\": {";
            cld.add(Calendar.YEAR,-1);
            String startTime = sdf.format(cld.getTime());
            String endTime = sdf.format(nowDate);
            searchStr += "\"gte\": \""+startTime+"\",\"lte\": \""+endTime+"\"";
            searchStr += "} } }";
        }else if("custom".equals(timeType)){
            if(StringUtils.isBlank(begin) || StringUtils.isBlank(end)){
                throw new Exception("时间参数异常,自定义时间开始和结束都不能为空");
            }else{
                searchStr = "{ \"range\": { \"attachment.date\": {";
                searchStr += "\"gte\": \""+begin+"\",\"lte\": \""+end+"\"";
                searchStr += "} } }";
            }
        }else{
            //什么也不是，异常情况
            throw new Exception("时间参数异常");
        }
        System.out.println(begin);
        System.out.println(end);
        System.out.println(timeType);

        return searchStr;
    }

    @PostMapping("insertClickLog")
    @ResponseBody
    public Map<String,Object> insertClickLog(HttpServletRequest request,int pageNumber,String clickType,int rankNumber,String linkUrl,String clickSearchId){
        //如果searchID为空就没必要执行插入语句，因为一定会报错
        try {
            if(StringUtils.isNotBlank(clickSearchId)){
                clickLogService.insertLog(request,clickType,pageNumber,rankNumber,linkUrl,clickSearchId);
            }else{
                logger.error("记录日志出错：点击日志searchID为空");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
