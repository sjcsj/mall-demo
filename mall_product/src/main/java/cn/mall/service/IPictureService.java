package cn.mall.service;

import cn.mall.domain.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPictureService extends IService<Picture> {

    MapAndCount1 getPicture(PageRequestParamWoId paramWoId);

}
