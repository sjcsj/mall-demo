package cn.mall.clients;

import cn.mall.common.R;
import cn.mall.domain.ShoppingCart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("mall-order")
@Component
public interface OrderClient {

    @PostMapping("/order")
    R createOrder(@RequestBody ShoppingCart shoppingCart);

    @GetMapping("/order/{id}")
    R getOrderById(@PathVariable("id") Integer id);

}
