package cn.mall.mapper;

import cn.mall.domain.TypeAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import javax.swing.*;
import java.util.List;

public interface TypeattributeMapper extends BaseMapper<TypeAttribute> {
    List<TypeAttribute> getByTypeId(Integer id);

    List<TypeAttribute> getTypeattributeByTypeId(Integer id);

}
