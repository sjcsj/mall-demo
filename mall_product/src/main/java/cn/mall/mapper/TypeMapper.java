package cn.mall.mapper;

import cn.mall.domain.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface TypeMapper extends BaseMapper<Type> {
    Type selectByType(String type);

    List<Type> getType(Integer a, Integer b, String keyword);

    Integer getCountByKeyword(String keyword);

    void amountincrease(Integer typeId);

    String getTypeName(Integer id);

    void amountdecrease(Integer typeId);

}
