package cn.mall.service.impl;

import cn.mall.domain.*;
import cn.mall.mapper.ProductMapper;
import cn.mall.mapper.ProductattributeMapper;
import cn.mall.mapper.TypeattributeMapper;
import cn.mall.service.IProductattributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ObjectStreamClass;
import java.util.*;

@Service
public class ProductattributeServiceImpl extends ServiceImpl<ProductattributeMapper, ProductAttribute> implements IProductattributeService {

    @Autowired
    private ProductattributeMapper productattributeMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TypeattributeMapper typeattributeMapper;

    @Override
    public ProductAttributeDetails1 getProductAttribute(PageRequestParamWoId paramWoId) {
        if (Objects.isNull(paramWoId.getPage())) {
            paramWoId.setPage(1);
        }
        if (Objects.isNull(paramWoId.getSize())) {
            paramWoId.setSize(10);
        }
        Integer a = paramWoId.getSize() * (paramWoId.getPage() - 1);
        Integer b = paramWoId.getSize();
        List<Product> products = productMapper.getProduct(a, b, paramWoId.getKeyword());
        Integer count = productMapper.getCountByKeyword(paramWoId.getKeyword());
        ProductAttributeDetails1 productAttributeDetails1 = new ProductAttributeDetails1();
        productAttributeDetails1.setCount(count);
        List<ProductAttributeDetails> list = new ArrayList<>();
        for (Product product : products) {
            ProductAttributeDetails productAttributeDetails = new ProductAttributeDetails();
            productAttributeDetails.setProduct(product);
            List<ProductAttribute> productAttributes =
                    productattributeMapper.getProductattributeByProductId(product.getId());
            Map<TypeAttribute, ProductAttribute> map = new HashMap<>();
            for (ProductAttribute productAttribute : productAttributes) {
                TypeAttribute typeAttribute = typeattributeMapper.selectById(productAttribute.getTypeattributeId());
                map.put(typeAttribute, productAttribute);
            }
            productAttributeDetails.setMap(map);
            list.add(productAttributeDetails);
        }
        productAttributeDetails1.setList(list);
        return productAttributeDetails1;
    }

    @Override
    public void deleteProductAttribute(Integer id) {
        ProductAttribute productAttribute = productattributeMapper.selectById(id);
        productattributeMapper.deleteById(productAttribute);
        productAttribute.setAttributevalue(null);
        productattributeMapper.insert(productAttribute);
    }
}
