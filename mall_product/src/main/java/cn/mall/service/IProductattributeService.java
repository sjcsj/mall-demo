package cn.mall.service;

import cn.mall.domain.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IProductattributeService extends IService<ProductAttribute> {
    ProductAttributeDetails1 getProductAttribute(PageRequestParamWoId paramWoId);

    void deleteProductAttribute(Integer id);

}
