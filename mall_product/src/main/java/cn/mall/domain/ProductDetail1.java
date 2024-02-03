package cn.mall.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductDetail1 {

    private Product product;

    private List<Picture> pictures;

    private Type type;

    private Map<TypeAttribute, ProductAttribute> map;

    private List<Evaluation> evaluations;
}
