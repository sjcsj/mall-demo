package cn.mall.mapper;

import cn.mall.domain.Address;
import cn.mall.domain.Product;
import cn.mall.domain.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    void deleteByUserId(Integer id);

    List<ShoppingCart> getByUserId(Integer id);

    Integer getCountByUserId(Integer id);

    Integer getSelectedproductByUserId(Integer id);

    List<ShoppingCart> selectByUserIdAndStatus(Integer userId);

    void updateall(Integer id);

    void cancelall(Integer id);

    ShoppingCart exist(Integer userId, Integer productId);

    void deleteByProductId(Integer productId);

}
