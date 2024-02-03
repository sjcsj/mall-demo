package cn.mall.service;

import cn.mall.domain.MapAndCount;
import cn.mall.domain.PageRequestParamWoId;
import cn.mall.domain.TypeAttribute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface ITypeattributeService extends IService<TypeAttribute> {
    MapAndCount show(PageRequestParamWoId paramWoId);

    Integer deleteTypeAttribute(Integer id);

    Integer updateTypeAttribute(TypeAttribute typeAttribute);

}
