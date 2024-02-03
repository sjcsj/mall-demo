package cn.mall.domain;

import lombok.Data;

import java.util.List;


@Data
public class OrdersAndCountAndTotalprice {

    private List<Order> orders;

    private Integer count;

    private double totalprice;
}
