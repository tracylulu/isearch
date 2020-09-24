package com.h3c.isearch.mapper;

import java.util.List;
import java.util.Map;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-07-03 13:42
 **/
public interface SearchRangeMapper {

    List<Map<String,Object>> queryRangeTable();
}
