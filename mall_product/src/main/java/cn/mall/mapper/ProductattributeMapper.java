package cn.mall.mapper;

import cn.mall.domain.ProductAttribute;
import cn.mall.domain.TypeAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ProductattributeMapper extends BaseMapper<ProductAttribute> {
    List<ProductAttribute> getProductattributeByProductId(Integer id);

    void deleteByProductId(Integer id);

    List<ProductAttribute> getByTypeattributeId(Integer id);

    void deleteByTypeattributeId(Integer id);

}
