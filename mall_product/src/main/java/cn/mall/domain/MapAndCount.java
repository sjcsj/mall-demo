package cn.mall.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MapAndCount {

    private Map<Type, List<TypeAttribute>> map;

    private Integer count;
}
