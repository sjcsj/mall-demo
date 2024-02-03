package cn.mall.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductAttributeDetails1 {

    private List<ProductAttributeDetails> list;

    private Integer count;

}
