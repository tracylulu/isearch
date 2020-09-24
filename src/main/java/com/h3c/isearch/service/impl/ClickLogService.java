package com.h3c.isearch.service.impl;

import com.h3c.isearch.mapper.ClickLogMapper;
import com.h3c.isearch.mapper.SearchLogMapper;
import com.h3c.isearch.service.IClickLogService;
import com.h3c.isearch.util.ReqUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-06-30 15:40
 **/

@Service
public class ClickLogService implements IClickLogService {

    @Autowired
    ClickLogMapper clickLogMapper;

    @Transactional
    @Override
    public Map<String, Object> insertLog(HttpServletRequest request, String clickType, int pageNumber, int rankNumber, String linkURL,String clickSearchId) {
        Map<String,Object> param = new HashMap();
        String userId = (String)request.getSession(false).getAttribute("userId");
        String Ip = ReqUtil.getIpAddress(request);
        if("next".equals(clickType)){
            clickType = "nextPage";
        }else if("prev".equals(clickType)){
            clickType = "prePage";
        }else if("page".equals(clickType)){
            clickType = "clickPage";
        }
        param.put("userId",userId);
        param.put("userIp",Ip);
        param.put("clickType",clickType);
        param.put("pageNumber",pageNumber);
        param.put("rankNumber",rankNumber);
        param.put("linkURL",linkURL);
        param.put("searchId",clickSearchId);
        System.out.println(clickSearchId);
        clickLogMapper.insertLog(param);
        return param;
    }
}
