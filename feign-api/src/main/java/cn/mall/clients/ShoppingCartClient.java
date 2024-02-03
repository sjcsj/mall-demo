package cn.mall.clients;

import cn.mall.common.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("mall-shoppingcart")
@Component
public interface ShoppingCartClient {

    @DeleteMapping("/shoppingcart/{id}")
    R deleteByUserId(@PathVariable("id") Integer id);

    @DeleteMapping("/shoppingcart/byProductId/{productId}")
    R deleteByProductId(@PathVariable("productId") Integer productId);
}
