package cn.mall.service.impl;

import cn.mall.clients.EvaluationClient;
import cn.mall.clients.ShoppingCartClient;
import cn.mall.clients.UserClient;
import cn.mall.common.MqConstants;
import cn.mall.domain.*;
import cn.mall.mapper.*;
import cn.mall.service.IProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import org.apache.lucene.search.similarities.Lambda;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private ProductattributeMapper productattributeMapper;

    @Autowired
    private EvaluationClient evaluationClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ShoppingCartClient shoppingCartClient;

    @Autowired
    private TypeattributeMapper typeattributeMapper;

    @Override
    public void addcollectednumber(Integer id) {
        productMapper.addcollectednumber(id);
    }

    @Override
    public void reducecollectednumber(Integer id) {
        productMapper.reducecollectednumber(id);
    }

    @Override
    public String getTypeById(Integer id) {
        Product product = productMapper.getById(id);
        Type type = typeMapper.selectById(product.getTypeId());
        return type.getType();
    }

    @Override
    public Product getProductByProductIdAndType(ProductIdAndType productIdAndType) {
        Type type = typeMapper.selectByType(productIdAndType.getType());
        Product product = productMapper.selectById(productIdAndType.getProductId());
        if (type.getId() == product.getTypeId()) {
            return product;
        }
        return null;
    }

    @Override
    public void addProduct(ProductDetail productDetail) {
        Product product = productDetail.getProduct();
        product.setAddtime(new Date());
        productMapper.insert(product);
        // 该类型的产品数量+1
        typeMapper.amountincrease(product.getTypeId());
        // 得到刚刚添加进去的产品的id
        Integer productId = productMapper.getIdByProductNameAndSnapShot(product.getProductname(), product.getSnapshot());
        // 图片可能一张都没有，需要if判断
        if (productDetail.getPictures().size() != 0) {
            for (String picture : productDetail.getPictures()) {
                Picture picture1 = new Picture();
                picture1.setProductId(productId);
                picture1.setPicture(picture);
                pictureMapper.insert(picture1);
            }
        }
        // 属性的值不一定有，但是属性一定有，所以无需if判断
        for (AttributeIdAndValue attributeIdAndValue : productDetail.getAttributeIdAndValues()) {
            ProductAttribute productAttribute = new ProductAttribute();
            productAttribute.setProductId(productId);
            productAttribute.setTypeattributeId(attributeIdAndValue.getTypeattributeId());
            productAttribute.setAttributevalue(attributeIdAndValue.getAttributevalue());
            productattributeMapper.insert(productAttribute);
        }
        // 判断该产品的状态是否为停售，若不是则加入索引库
        if (product.getProductstatus() == 2) {
            return;
        }
        product.setId(productId);
        product.setVolume(0);
        Product1 product1 = new Product1(product);
        String typeName = typeMapper.getTypeName(product.getTypeId());
        product1.setType(typeName);
        product1.addsuggestion();
        // 更新索引库
        rabbitTemplate.convertAndSend(MqConstants.MALL_EXCHANGE,
                MqConstants.MALL_INSERT_KEY, product1);
    }

    @Override
    public ProductDetail1 getProductDetailById(Integer id) {
        Product product = productMapper.selectById(id);
        Type type = typeMapper.selectById(product.getTypeId());
        List<Picture> pictures = pictureMapper.getByProductId(product.getId());
        List<ProductAttribute> productAttributes = productattributeMapper.getProductattributeByProductId(product.getId());
        Map<TypeAttribute, ProductAttribute> map = new HashMap<>();
        for (ProductAttribute productAttribute : productAttributes) {
            TypeAttribute typeAttribute = typeattributeMapper.selectById(productAttribute.getTypeattributeId());
            map.put(typeAttribute, productAttribute);
        }
        List<Evaluation> evaluations = evaluationClient.getEvaluation(product.getId());
        ProductDetail1 productDetail1 = new ProductDetail1();
        productDetail1.setProduct(product);
        productDetail1.setType(type);
        productDetail1.setPictures(pictures);
        productDetail1.setMap(map);
        productDetail1.setEvaluations(evaluations);
        return productDetail1;
    }

    @Override
    public void updateProduct(ProductDetail2 productDetail2) {
        Product product2 = productMapper.selectById(productDetail2.getProduct().getId());
        // 查看类型是改变，若改变，则原来类型的产品数-1，现在所属类型的产品数＋1
        if (product2.getTypeId() != productDetail2.getProduct().getTypeId()) {
            typeMapper.amountdecrease(product2.getTypeId());
            typeMapper.amountincrease(productDetail2.getProduct().getTypeId());
        }
        // 删除之前添加的图片，产品属性值
        productattributeMapper.deleteByProductId(productDetail2.getProduct().getId());
        pictureMapper.deleteByProductId(productDetail2.getProduct().getId());
        productMapper.updateById(productDetail2.getProduct());
        for (Picture picture : productDetail2.getPictures()) {
            pictureMapper.insert(picture);
        }
        for (ProductAttribute productAttribute : productDetail2.getProductAttributes()) {
            productattributeMapper.insert(productAttribute);
        }
        // 判断该产品的状态是否为停售，若不是则更新索引库，若是则删除索引库中与之对应数据
        if (productDetail2.getProduct().getProductstatus() == 2) {
            // 删除有关该产品的购物车数据
            shoppingCartClient.deleteByProductId(productDetail2.getProduct().getId());
            // 删除该产品的索引库数据
            rabbitTemplate.convertAndSend(MqConstants.MALL_EXCHANGE,
                    MqConstants.MALL_DELETE_KEY, productDetail2.getProduct().getId());
            return;
        }
        Product product = productDetail2.getProduct();
        Product1 product1 = new Product1(product);
        String typeName = typeMapper.getTypeName(product.getTypeId());
        product1.setType(typeName);
        product1.addsuggestion();
        // 更新索引库
        rabbitTemplate.convertAndSend(MqConstants.MALL_EXCHANGE, MqConstants.MALL_UPDATE_KEY,
                product1);
    }

//    @Override
//    public void deleteProduct(Integer id) {
//        Product product = productMapper.selectById(id);
//        // 删除图片
//        pictureMapper.deleteByProductId(id);
//        // 该产品对应类型的产品数量-1
//        typeMapper.amountdecrease(product.getTypeId());
//        // 删除该产品的所有评论
//        evaluationClient.deleteByProductId(id);
//        // 删除该产品的属性值
//        productattributeMapper.deleteByProductId(id);
//        // 删除用户有关该产品的收藏记录
//        userClient.deleteCollectionByProductId(id);
//        // 删除有关该产品的购物车记录
//        shoppingCartClient.deleteByProductId(id);
//        // 删除产品
//        productMapper.deleteById(product);
//        // 更新索引库
//        rabbitTemplate.convertAndSend(MqConstants.MALL_EXCHANGE, MqConstants.MALL_DELETE_KEY, id);
//    }

    @Override
    public IPage<Product> listProduct(PageRequestParamWoIdPlus paramWoIdPlus) {
        if (Objects.isNull(paramWoIdPlus.getPage())) {
            paramWoIdPlus.setPage(1);
        }
        if (Objects.isNull(paramWoIdPlus.getSize())) {
            paramWoIdPlus.setSize(10);
        }
        IPage<Product> productIPage = new Page<>(paramWoIdPlus.getPage(), paramWoIdPlus.getSize());
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!Strings.isNullOrEmpty(paramWoIdPlus.getNameKeyword()),
                Product::getProductname, paramWoIdPlus.getNameKeyword());
        queryWrapper.like(!Strings.isNullOrEmpty(paramWoIdPlus.getBrandKeyword()),
                Product::getBrand, paramWoIdPlus.getBrandKeyword());
        queryWrapper.eq(paramWoIdPlus.getProductstatus() != null,
                Product::getProductstatus, paramWoIdPlus.getProductstatus());
        productMapper.selectPage(productIPage, queryWrapper);
        return productIPage;
    }

    @Override
    public ProductDetail1 getProductDetail1ById(Integer id) {
        Product product = productMapper.selectById(id);
        Type type = typeMapper.selectById(product.getTypeId());
        List<Picture> pictures = pictureMapper.getByProductId(product.getId());
        List<ProductAttribute> productAttributes = productattributeMapper.getProductattributeByProductId(product.getId());
        Map<TypeAttribute, ProductAttribute> map = new HashMap<>();
        for (ProductAttribute productAttribute : productAttributes) {
            if (Strings.isNullOrEmpty(productAttribute.getAttributevalue())) {
                continue;
            }
            TypeAttribute typeAttribute = typeattributeMapper.selectById(productAttribute.getTypeattributeId());
            map.put(typeAttribute, productAttribute);
        }
        List<Evaluation> evaluations = evaluationClient.getEvaluation(product.getId());
        ProductDetail1 productDetail1 = new ProductDetail1();
        productDetail1.setProduct(product);
        productDetail1.setType(type);
        productDetail1.setPictures(pictures);
        productDetail1.setMap(map);
        productDetail1.setEvaluations(evaluations);
        return productDetail1;
    }

//    @Override
//    public void checkStock() {
//        List<Product> products = productMapper.checkStock();
//        for (Product product : products) {
//            product.setProductstatus(2);
//            productMapper.updateById(product);
//            // 删除有关该产品的购物车数据
//            shoppingCartClient.deleteByProductId(product.getId());
//            // 更新索引库
//            rabbitTemplate.convertAndSend(MqConstants.MALL_EXCHANGE,
//                    MqConstants.MALL_DELETE_KEY, product.getId());
//        }
//    }

    @Override
    public void change(IdAndAmount idAndAmount) {
        productMapper.change(idAndAmount);
        Product product = productMapper.selectById(idAndAmount.getId());
        if (product.getStock() == 0) {
            product.setProductstatus(2);
            productMapper.updateById(product);
            rabbitTemplate.convertAndSend(MqConstants.MALL_EXCHANGE, MqConstants.MALL_DELETE_KEY,
                    idAndAmount.getId());
        }
    }

    @Override
    public void change1(IdAndAmount idAndAmount) {
        productMapper.change1(idAndAmount);
        Product product = productMapper.selectById(idAndAmount.getId());
        Product1 product1 = new Product1(product);
        String typeName = typeMapper.getTypeName(product.getTypeId());
        product1.setType(typeName);
        product1.addsuggestion();
        rabbitTemplate.convertAndSend(MqConstants.MALL_EXCHANGE, MqConstants.MALL_UPDATE_KEY,
                product1);
    }

    @Override
    public void change2(IdAndAmount idAndAmount) {
        Product product = productMapper.selectById(idAndAmount.getId());
        // 判断该产品之前是否已经变为停售状态
        if (product.getProductstatus() == 2) {
            product.setProductstatus(0);
            productMapper.updateById(product);
            productMapper.change2(idAndAmount);
            Product product2 = productMapper.selectById(idAndAmount.getId());
            Product1 product1 = new Product1(product);
            String typeName = typeMapper.getTypeName(product2.getTypeId());
            product1.setType(typeName);
            product1.addsuggestion();
            rabbitTemplate.convertAndSend(MqConstants.MALL_EXCHANGE,
                    MqConstants.MALL_INSERT_KEY, product1);
        }else {
            productMapper.change2(idAndAmount);
        }
    }

}
