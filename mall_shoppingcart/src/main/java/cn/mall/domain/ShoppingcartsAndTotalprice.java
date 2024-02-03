package cn.mall.domain;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingcartsAndTotalprice {

    private List<ShoppingCart> shoppingCarts;

    private double totalprice;
}
