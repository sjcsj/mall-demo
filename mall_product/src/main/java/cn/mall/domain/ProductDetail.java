package cn.mall.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetail {

    private Product product;

    private List<String> pictures;

    private List<AttributeIdAndValue> attributeIdAndValues;

}
