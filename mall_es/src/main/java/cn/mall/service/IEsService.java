package cn.mall.service;

import cn.mall.domain.PageResult;
import cn.mall.domain.Product;
import cn.mall.domain.Product1;
import cn.mall.domain.RequestParams;

import java.util.List;
import java.util.Map;

public interface IEsService {
    PageResult search(RequestParams requestParams);

    Map<String, List<String>> aggregation(RequestParams params);

    List<String> getSuggestions(String prefix);

    void insert(Product1 product1);

    void update(Product1 product1);

    void deleteById(Integer id);


}
