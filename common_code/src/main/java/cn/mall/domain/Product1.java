package cn.mall.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product1 {

    private Integer id;

    private String type;

    private String productname;

    private String snapshot;

    private String brand;

    private double price;

    private Integer volume;

    private boolean isad;

    private List<String> suggestion;

    public Product1(Product product) {
        this.id = product.getId();
        this.productname = product.getProductname();
        this.snapshot = product.getSnapshot();
        this.brand = product.getBrand();
        // 如果产品状态为停售就不存进索引库
        if (product.getProductstatus() == 0) {
            this.price = product.getPrice();
        } else {
            this.price = product.getSaleprice();
        }
        this.volume = product.getVolume();
        if (product.getIsad() == 1) {
            this.isad = true;
        } else {
            this.isad = false;
        }
    }

    public void addsuggestion(){
        this.suggestion = Arrays.asList(this.brand, this.type);
    }
}
