package cn.mall.service;

import cn.mall.domain.PageRequestParamWoIdAndAmount;

import cn.mall.domain.Product;
import cn.mall.domain.Type;
import cn.mall.domain.TypeAttribute;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ITypeService extends IService<Type> {

    IPage<Type> getType(PageRequestParamWoIdAndAmount paramWoId);

    Integer deleteType(Integer id);

    List<TypeAttribute> getTypeattributeByTypeId(Integer id);

    List<Product> getProductByTypeId(Integer id);

    String getTypeName(Integer id);

}
