package cn.mall.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductAttributeDetails {

    private Product product;

    private Map<TypeAttribute, ProductAttribute> map;
}
