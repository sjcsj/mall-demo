package cn.mall.service.impl;

import cn.mall.domain.PageRequestParamWoIdAndAmount;
import cn.mall.domain.Product;
import cn.mall.domain.Type;
import cn.mall.domain.TypeAttribute;
import cn.mall.mapper.ProductMapper;
import cn.mall.mapper.TypeMapper;
import cn.mall.mapper.TypeattributeMapper;
import cn.mall.service.ITypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TypeattributeMapper typeattributeMapper;

    @Override
    public IPage<Type> getType(PageRequestParamWoIdAndAmount paramWoId) {
        if (Objects.isNull(paramWoId.getPage())) {
            paramWoId.setPage(1);
        }
        if (Objects.isNull(paramWoId.getSize())) {
            paramWoId.setSize(10);
        }
        IPage<Type> typeIPage = new Page<>(paramWoId.getPage(), paramWoId.getSize());
        LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(paramWoId.getMinnumber() != null, Type::getAmount, paramWoId.getMinnumber());
        queryWrapper.le(paramWoId.getMaxnumber() != null, Type::getAmount, paramWoId.getMaxnumber());
        queryWrapper.like(!Strings.isNullOrEmpty(paramWoId.getKeyword()), Type::getType, paramWoId.getKeyword());
        typeMapper.selectPage(typeIPage, queryWrapper);
        return typeIPage;
    }

    @Override
    public Integer deleteType(Integer id) {
        Type type = typeMapper.selectById(id);
        if (type.getAmount() == 0) {
            typeMapper.deleteById(id);
            return 1;
        }
        return 0;
    }

    @Override
    public List<TypeAttribute> getTypeattributeByTypeId(Integer id) {
        List<TypeAttribute> typeattributeByTypeId = typeattributeMapper.getTypeattributeByTypeId(id);
        return typeattributeByTypeId;
    }

    @Override
    public List<Product> getProductByTypeId(Integer id) {
        List<Product> productByTypeId = productMapper.getProductByTypeId(id);
        return productByTypeId;
    }

    @Override
    public String getTypeName(Integer id) {
        String typeName = typeMapper.getTypeName(id);
        return typeName;
    }
}










