package cn.mall.domain;

import lombok.Data;

import java.util.List;

@Data
public class UserIdAndProductIds {

    private Integer userId;

    private List<Integer> productId;
}
