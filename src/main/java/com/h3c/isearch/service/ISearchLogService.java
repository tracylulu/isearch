package com.h3c.isearch.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public interface ISearchLogService {

    Map<String,Object> insertLog(HttpServletRequest request, String searchJsonStr, String resultStr, Date reqDate, Date responDate);
}
