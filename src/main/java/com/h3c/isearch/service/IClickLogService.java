package com.h3c.isearch.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-06-30 15:38
 **/
public interface IClickLogService {

    Map<String,Object> insertLog(HttpServletRequest request, String clickType, int pageNumber, int rankNumber, String linkURL,String clickSearchId);
}
