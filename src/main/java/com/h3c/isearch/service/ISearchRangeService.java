package com.h3c.isearch.service;

import java.util.List;
import java.util.Map;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-07-03 13:24
 **/
public interface ISearchRangeService {

    List<Map<String,Object>>  queryRangeTable();
}
