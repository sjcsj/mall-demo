package cn.mall.service.impl;

import cn.mall.domain.*;
import cn.mall.mapper.ProductattributeMapper;
import cn.mall.mapper.TypeMapper;
import cn.mall.mapper.TypeattributeMapper;
import cn.mall.service.ITypeattributeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TypeattributeServiceImpl extends ServiceImpl<TypeattributeMapper, TypeAttribute> implements ITypeattributeService {

    @Autowired
    private TypeattributeMapper typeattributeMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private ProductattributeMapper productattributeMapper;

    @Override
    public MapAndCount show(PageRequestParamWoId paramWoId) {
        if (Objects.isNull(paramWoId.getPage())) {
            paramWoId.setPage(1);
        }
        if (Objects.isNull(paramWoId.getSize())) {
            paramWoId.setSize(10);
        }
        Integer a = paramWoId.getSize() * (paramWoId.getPage() - 1);
        Integer b = paramWoId.getSize();
        List<Type> types = typeMapper.getType(a, b, paramWoId.getKeyword());
        Integer count = typeMapper.getCountByKeyword(paramWoId.getKeyword());
        Map<Type, List<TypeAttribute>> map = new HashMap<>();
        for (Type type : types) {
            List<TypeAttribute> byTypeId = typeattributeMapper.getByTypeId(type.getId());
            map.put(type, byTypeId);
        }
        MapAndCount mapAndCount = new MapAndCount();
        mapAndCount.setMap(map);
        mapAndCount.setCount(count);
        return mapAndCount;
    }

    @Override
    public Integer deleteTypeAttribute(Integer id) {
        List<ProductAttribute> productAttributes = productattributeMapper.getByTypeattributeId(id);
        if (productAttributes.size() == 0) {
            typeattributeMapper.deleteById(id);
            return 1;
        }
        for (ProductAttribute productAttribute : productAttributes) {
            if (!Objects.isNull(productAttribute.getAttributevalue())){
                return 0;
            }
        }
        productattributeMapper.deleteByTypeattributeId(id);
        typeattributeMapper.deleteById(id);
        return 1;
    }

    @Override
    public Integer updateTypeAttribute(TypeAttribute typeAttribute) {
        List<ProductAttribute> productAttributes = productattributeMapper.getByTypeattributeId(typeAttribute.getId());
        if (productAttributes.size() == 0) {
            typeattributeMapper.updateById(typeAttribute);
            return 1;
        }
        for (ProductAttribute productAttribute : productAttributes) {
            if (!Objects.isNull(productAttribute.getAttributevalue())){
                return 0;
            }
        }
        typeattributeMapper.updateById(typeAttribute);
        return 1;
    }
}
