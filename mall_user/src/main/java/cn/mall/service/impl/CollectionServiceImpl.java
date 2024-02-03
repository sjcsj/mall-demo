package cn.mall.service.impl;

import cn.mall.clients.ProductClient;
import cn.mall.domain.*;
import cn.mall.domain.Collection;
import cn.mall.mapper.CollectionMapper;
import cn.mall.mapper.UserMapper;
import cn.mall.service.ICollectionService;
import cn.mall.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements ICollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private ProductClient productClient;

    @Override
    public void removeByUserIdAndProductId(Collection collection) {
        collectionMapper.deleteByUserIdAndProductId(collection.getUserId(),
                collection.getProductId());
        productClient.reducecollectednumber(collection.getProductId());
    }

    @Override
    public void addCollection(Collection collection) {
        collection.setCreatetime(new Date());
        collectionMapper.insert(collection);
        productClient.addcollectednumber(collection.getProductId());
    }

    @Override
    public Integer collectionnumber(Integer id) {
        Integer collectionnumber = collectionMapper.collectionnumber(id);
        return collectionnumber;
    }

    @Override
    public List<ProductAndCollectiontime> getProductByUserId(Integer id) {
        List<Collection> collectionList = collectionMapper.getCollectionsByUserId(id);
        if (collectionList.size() == 0) {
            return null;
        }
        List<ProductAndCollectiontime> productAndCollectiontimes = new ArrayList<>();
        for (Collection collection : collectionList) {
            ProductAndCollectiontime productAndCollectiontime = new ProductAndCollectiontime();
            productAndCollectiontime.setCollectiontime(collection.getCreatetime());
            Product productById = productClient.getProductById(collection.getProductId());
            productAndCollectiontime.setProduct(productById);
            productAndCollectiontimes.add(productAndCollectiontime);
        }
        // 按照时间先后顺序进行排序
        productAndCollectiontimes.sort(new Comparator<ProductAndCollectiontime>() {
            @Override
            public int compare(ProductAndCollectiontime o1, ProductAndCollectiontime o2) {
                long time1 = o1.getCollectiontime().getTime();
                long time2 = o2.getCollectiontime().getTime();
                if (time2 > time1) {
                    return 1;
                } else if (time2 < time1) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return productAndCollectiontimes;
    }

    @Override
    public Set<String> getTypeByUserId(Integer id) {
        List<Collection> collectionList = collectionMapper.getCollectionsByUserId(id);
        if (collectionList.size() == 0) {
            return null;
        }
        Set<String> typeList = new HashSet<>();
        for (Collection collection : collectionList) {
            System.out.println(collection.getProductId());
            String type = productClient.getTypeById(collection.getProductId());
            typeList.add(type);
        }
        return typeList;
    }

    @Override
    public List<ProductAndCollectiontime> getProductByUserIdAndType(UserIdAndType userIdAndType) {
        List<Collection> collectionList =
                collectionMapper.getCollectionsByUserId(userIdAndType.getUserId());
        if (collectionList.size() == 0) {
            return null;
        }
        List<ProductAndCollectiontime> productAndCollectiontimes = new ArrayList<>();
        for (Collection collection : collectionList) {
            ProductAndCollectiontime productAndCollectiontime = new ProductAndCollectiontime();
            productAndCollectiontime.setCollectiontime(collection.getCreatetime());
            ProductIdAndType productIdAndType = new ProductIdAndType(collection.getProductId(), userIdAndType.getType());
            Product product = productClient.getProductByProductIdAndType(productIdAndType);
            if (Objects.isNull(product)) {
                continue;
            } else {
                productAndCollectiontime.setProduct(product);
            }
            productAndCollectiontimes.add(productAndCollectiontime);
        }
        // 按照时间先后顺序进行排序
        productAndCollectiontimes.sort(new Comparator<ProductAndCollectiontime>() {
            @Override
            public int compare(ProductAndCollectiontime o1, ProductAndCollectiontime o2) {
                long time1 = o1.getCollectiontime().getTime();
                long time2 = o2.getCollectiontime().getTime();
                if (time2 > time1) {
                    return 1;
                } else if (time2 < time1) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return productAndCollectiontimes;
    }

    @Override
    public void deleteCollectionByIds(UserIdAndProductIds userIdAndProductIds) {
        Integer userId = userIdAndProductIds.getUserId();
        List<Integer> productId = userIdAndProductIds.getProductId();
        for (Integer id : productId) {
            collectionMapper.deleteByUserIdAndProductId(userId, id);
            productClient.reducecollectednumber(id);
        }
    }

    @Override
    public boolean exist(ShoppingCart shoppingCart) {
        Collection exist = collectionMapper.exist(shoppingCart.getUserId(), shoppingCart.getProductId());
        if (Objects.isNull(exist)){
            return false;
        }
        return true;
    }

    @Override
    public void deleteCollectionByProductId(Integer productId) {
        collectionMapper.deleteCollectionByProductId(productId);
    }
}
