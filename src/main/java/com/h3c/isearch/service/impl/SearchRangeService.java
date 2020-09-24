package com.h3c.isearch.service.impl;

import com.h3c.isearch.mapper.SearchRangeMapper;
import com.h3c.isearch.service.ISearchRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: iSearch
 * @description:
 * @author: zhanghao
 * @create: 2020-07-03 13:26
 **/
@Service
public class SearchRangeService implements ISearchRangeService {

    @Autowired
    SearchRangeMapper searchRangeMapper;

    @Override
    public List<Map<String, Object>> queryRangeTable() {
        List<Map<String, Object>> rangeTable = searchRangeMapper.queryRangeTable();
        return rangeTable;
    }

}
