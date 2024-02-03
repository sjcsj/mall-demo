package cn.mall.mapper;


import cn.mall.domain.Picture;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PictureMapper extends BaseMapper<Picture> {
    List<Picture> getPicturesByProductId(Integer id);

    List<Picture> getByProductId(Integer id);

    void deleteByProductId(Integer id);

}
