package cn.mall.service.impl;

import cn.mall.domain.*;
import cn.mall.mapper.PictureMapper;
import cn.mall.mapper.ProductMapper;
import cn.mall.service.IPictureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public MapAndCount1 getPicture(PageRequestParamWoId paramWoId) {
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
        MapAndCount1 mapAndCount1 = new MapAndCount1();
        mapAndCount1.setCount(count);
        Map<Product, List<Picture>> map = new HashMap<>();
        for (Product product : products) {
            List<Picture> pictures = pictureMapper.getPicturesByProductId(product.getId());
            map.put(product, pictures);
        }
        mapAndCount1.setMap(map);
        return mapAndCount1;
    }
}
