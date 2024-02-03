package cn.mall.domain;

import lombok.Data;

@Data
public class RequestParams {

    private String key;

    private Integer page;

    private Integer size;

    private Integer sort;

    private String brand;

    private String type;

    private Integer minPrice;

    private Integer maxPrice;
}
