package cn.mall.service;

import cn.mall.domain.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

public interface ICollectionService extends IService<Collection> {
    void removeByUserIdAndProductId(Collection collection);

    void addCollection(Collection collection);

    Integer collectionnumber(Integer id);

    List<ProductAndCollectiontime> getProductByUserId(Integer id);

    Set<String> getTypeByUserId(Integer id);

    List<ProductAndCollectiontime> getProductByUserIdAndType(UserIdAndType userIdAndType);

    void deleteCollectionByIds(UserIdAndProductIds userIdAndProductIds);

    boolean exist(ShoppingCart shoppingCart);

    void deleteCollectionByProductId(Integer productId);

}
