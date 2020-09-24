package com.h3c.isearch.service.impl;

import com.h3c.isearch.mapper.SearchLogMapper;
import com.h3c.isearch.service.ISearchLogService;
import com.h3c.isearch.util.ReqUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-06-30 15:39
 **/

@Service
public class SearchLogService implements ISearchLogService {

    @Autowired
    SearchLogMapper searchLogMapper;

    @Transactional
    @Override
    public Map<String,Object> insertLog(HttpServletRequest request, String searchJsonStr, String resultStr, Date reqDate, Date responDate) {
        Map<String,Object> param = new HashMap();
        String userId = (String)request.getSession(false).getAttribute("userId");
        String Ip = ReqUtil.getIpAddress(request);
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        param.put("userId",userId);
        param.put("userIp",Ip);
        param.put("searchType","simple");
        param.put("reqBody",searchJsonStr);
        param.put("reqTime",reqDate);
        param.put("resTime",responDate);
        param.put("resBody",resultStr);
        param.put("resBody",resultStr);
        param.put("searchId",uuid);
        searchLogMapper.insertLog(param);
        return param;
    }
}
