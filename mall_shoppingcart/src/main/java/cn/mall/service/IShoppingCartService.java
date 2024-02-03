package cn.mall.service;

import cn.mall.domain.ShoppingcartsAndTotalprice;
import cn.mall.domain.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IShoppingCartService extends IService<ShoppingCart> {
    void deleteByUserId(Integer id);

    void add(ShoppingCart shoppingCart);

    ShoppingcartsAndTotalprice getInfoByUserId(Integer id);

    Integer getCountByUserId(Integer id);

    Integer getSelectedproductByUserId(Integer id);

    void addCollection(ShoppingCart shoppingCart);

    Integer addCollections(Integer userId);

    Integer deleteTick(Integer userId);

    void updateall(Integer id);

    void cancelall(Integer id);

    Integer judge(Integer id);

    Integer settlement(Integer userId);

    void deleteByProductId(Integer productId);


}
