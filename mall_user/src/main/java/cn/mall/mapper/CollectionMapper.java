package cn.mall.mapper;

import cn.mall.domain.Collection;
import cn.mall.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface CollectionMapper extends BaseMapper<Collection> {
    void deleteByUserId(Integer id);

    void deleteByUserIdAndProductId(Integer userId, Integer productId);

    Integer collectionnumber(Integer id);

    List<Collection> getCollectionsByUserId(Integer id);

    Collection exist(Integer userId, Integer productId);

    void deleteCollectionByProductId(Integer productId);

}
