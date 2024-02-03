package cn.mall.service.impl;

import cn.mall.clients.UserClient;
import cn.mall.clients.OrderClient;
import cn.mall.clients.ProductClient;
import cn.mall.common.R;
import cn.mall.domain.*;
import cn.mall.mapper.ShoppingCartMapper;
import cn.mall.service.IShoppingCartService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderClient orderClient;

    @Override
    public void deleteByUserId(Integer id) {
        shoppingCartMapper.deleteByUserId(id);
    }

    @Override
    public void add(ShoppingCart shoppingCart) {
        // 判断之前是否已经添加该产品到购物车，如果有，只需将数量叠加
        ShoppingCart exist = shoppingCartMapper.exist(shoppingCart.getUserId(), shoppingCart.getProductId());
        if (!Objects.isNull(exist)) {
            Integer amount = exist.getAmount();
            exist.setAmount(amount + shoppingCart.getAmount());
            shoppingCartMapper.updateById(exist);
        } else {
            shoppingCart.setStatus(0);
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public ShoppingcartsAndTotalprice getInfoByUserId(Integer id) {
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.getByUserId(id);
        List<ShoppingCart> shoppingCarts1 = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            Product product = productClient.getProductById(shoppingCart.getProductId());
            shoppingCart.setProduct(product);
            // 商品当前价格 * 商品数量
            if (product.getProductstatus() == 0) {
                double v = product.getPrice() * shoppingCart.getAmount();
                shoppingCart.setMoney(v);
            } else {
                double v = product.getSaleprice() * shoppingCart.getAmount();
                shoppingCart.setMoney(v);
            }
            shoppingCarts1.add(shoppingCart);
        }
        double totalprice = 0;
        for (ShoppingCart shoppingCart : shoppingCarts1) {
            if (shoppingCart.getStatus() == 1) {
                totalprice = totalprice + shoppingCart.getMoney();
            }
        }
        ShoppingcartsAndTotalprice shoppingcartsAndTotalprice = new ShoppingcartsAndTotalprice();
        shoppingcartsAndTotalprice.setShoppingCarts(shoppingCarts1);
        shoppingcartsAndTotalprice.setTotalprice(totalprice);
        return shoppingcartsAndTotalprice;
    }

    @Override
    public Integer getCountByUserId(Integer id) {
        Integer count = shoppingCartMapper.getCountByUserId(id);
        return count;
    }

    @Override
    public Integer getSelectedproductByUserId(Integer id) {
        Integer count = shoppingCartMapper.getSelectedproductByUserId(id);
        return count;
    }

    @Override
    public void addCollection(ShoppingCart shoppingCart) {
        boolean exist = userClient.exist(shoppingCart);
        if (exist) {
            shoppingCartMapper.deleteById(shoppingCart);
        } else {
            Collection collection = new Collection();
            collection.setUserId(shoppingCart.getUserId());
            collection.setProductId(shoppingCart.getProductId());
            userClient.add(collection);
            shoppingCartMapper.deleteById(shoppingCart);
        }
    }

    @Override
    public Integer addCollections(Integer userId) {
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserIdAndStatus(userId);
        if (shoppingCarts.size() == 0) {
            return 0;
        }
        for (ShoppingCart shoppingCart : shoppingCarts) {
            boolean exist = userClient.exist(shoppingCart);
            if (exist) {
                shoppingCartMapper.deleteById(shoppingCart);
            } else {
                Collection collection = new Collection();
                collection.setUserId(shoppingCart.getUserId());
                collection.setProductId(shoppingCart.getProductId());
                userClient.add(collection);
                shoppingCartMapper.deleteById(shoppingCart);
            }
        }
        return 1;
    }

    @Override
    public Integer deleteTick(Integer userId) {
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserIdAndStatus(userId);
        if (shoppingCarts.size() == 0) {
            return 0;
        }
        for (ShoppingCart shoppingCart : shoppingCarts) {
            shoppingCartMapper.deleteById(shoppingCart);
        }
        return 1;
    }

    @Override
    public void updateall(Integer id) {
        shoppingCartMapper.updateall(id);
    }

    @Override
    public void cancelall(Integer id) {
        shoppingCartMapper.cancelall(id);
    }

    @Override
    public Integer judge(Integer id) {
        Integer count = shoppingCartMapper.getCountByUserId(id);
        Integer count1 = shoppingCartMapper.getSelectedproductByUserId(id);
        if (count == count1) {
            if (count == 0) {
                return 0;
            }
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Integer settlement(Integer userId) {
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserIdAndStatus(userId);
        if (shoppingCarts.size() == 0) {
            return 0;
        }
        Integer addressexist = userClient.addressexist(userId);
        if (addressexist == 0) {
            return 1;
        }
        for (ShoppingCart shoppingCart : shoppingCarts) {
            orderClient.createOrder(shoppingCart);
        }
        return 2;
    }

    @Override
    public void deleteByProductId(Integer productId) {
        shoppingCartMapper.deleteByProductId(productId);
    }
}
