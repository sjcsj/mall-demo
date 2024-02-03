package cn.mall.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MapAndCount1 {

    private Map<Product, List<Picture>> map;

    private Integer count;
}
