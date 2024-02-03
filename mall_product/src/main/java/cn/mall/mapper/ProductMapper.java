package cn.mall.mapper;

import cn.mall.domain.IdAndAmount;
import cn.mall.domain.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


public interface ProductMapper extends BaseMapper<Product> {
    void addcollectednumber(Integer id);

    void reducecollectednumber(Integer id);

    Product getById(Integer id);

    List<Product> getProductByTypeId(Integer id);

    Integer getIdByProductNameAndSnapShot(String productname, String snapshot);

    List<Product> getProduct(Integer a, Integer b, String keyword);

    Integer getCountByKeyword(String keyword);

    List<Product> checkStock();

    void change(IdAndAmount idAndAmount);

    void change1(IdAndAmount idAndAmount);

    void change2(IdAndAmount idAndAmount);

}
