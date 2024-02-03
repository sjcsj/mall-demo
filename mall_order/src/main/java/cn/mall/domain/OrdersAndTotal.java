package cn.mall.domain;

import lombok.Data;

import java.util.List;

@Data
public class OrdersAndTotal {

    private List<Order> orders;

    private Integer total;
}
