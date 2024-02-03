package cn.mall.clients;

import cn.mall.common.R;
import cn.mall.domain.IdAndAmount;
import cn.mall.domain.Product;
import cn.mall.domain.ProductIdAndType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mall-product")
@Component
public interface ProductClient {

    @PutMapping("/product/increase/{id}")
    R addcollectednumber(@PathVariable("id") Integer id);

    @PutMapping("/product/decrease/{id}")
    R reducecollectednumber(@PathVariable("id") Integer id);

    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable("id") Integer id);

    @GetMapping("/product/type/{id}")
    String getTypeById(@PathVariable("id") Integer id);

    @GetMapping("/product")
    Product getProductByProductIdAndType(@RequestBody ProductIdAndType productIdAndType);

    @GetMapping("/type/typename/{id}")
    String getTypeName(@PathVariable("id") Integer id);

    @PutMapping("/product/change")
    R change(@RequestBody IdAndAmount idAndAmount);

    @PutMapping("/product/change1")
    R change1(@RequestBody IdAndAmount idAndAmount);

    @PutMapping("/product/change2")
    R change2(@RequestBody IdAndAmount idAndAmount);
}
