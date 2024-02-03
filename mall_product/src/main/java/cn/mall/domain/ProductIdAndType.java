package cn.mall.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductIdAndType {

    private Integer productId;

    private String type;
}
