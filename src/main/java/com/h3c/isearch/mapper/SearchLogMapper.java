package com.h3c.isearch.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-06-30 11:17
 **/
public interface SearchLogMapper {

    int insertLog(Map<String,Object> param);
}
