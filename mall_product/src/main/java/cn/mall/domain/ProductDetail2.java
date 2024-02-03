package cn.mall.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetail2 {

    private Product product;

    private List<Picture> pictures;

    private Type type;

    private List<ProductAttribute> productAttributes;
}
