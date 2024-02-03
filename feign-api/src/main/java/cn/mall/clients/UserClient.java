package cn.mall.clients;

import cn.mall.common.R;
import cn.mall.domain.Collection;
import cn.mall.domain.ShoppingCart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@FeignClient("mall-user")
@Component
public interface UserClient {

    @GetMapping("/address/{id}")
    R getAddressByUserId(@PathVariable("id") Integer id);

    @GetMapping("/address/detail/{id}")
    R getAddressById(@PathVariable("id") Integer id);

    @GetMapping("/address/exist/{userId}")
    Integer addressexist(@PathVariable("userId") Integer userId);

    @PostMapping("/collection/exist")
    boolean exist(@RequestBody ShoppingCart shoppingCart);

    @PostMapping("/collection")
    R add(@RequestBody Collection collection);

    @GetMapping("/user/{id}")
    R getUserById(@PathVariable("id") Integer id);

    @DeleteMapping("/collection/deleteByProduct/{productId}")
    R deleteCollectionByProductId(@PathVariable("productId") Integer productId);

}
