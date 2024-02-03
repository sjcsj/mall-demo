package cn.mall.service;

import cn.mall.domain.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IProductService extends IService<Product> {

    void addcollectednumber(Integer id);

    void reducecollectednumber(Integer id);

    String getTypeById(Integer id);

    Product getProductByProductIdAndType(ProductIdAndType productIdAndType);

    void addProduct(ProductDetail productDetail);

    ProductDetail1 getProductDetailById(Integer id);

    void updateProduct(ProductDetail2 productDetail2);

//    void deleteProduct(Integer id);

    IPage<Product> listProduct(PageRequestParamWoIdPlus pageRequestParamWoIdPlus);

    ProductDetail1 getProductDetail1ById(Integer id);

//    void checkStock();

    void change(IdAndAmount idAndAmount);

    void change1(IdAndAmount idAndAmount);

    void change2(IdAndAmount idAndAmount);

}
