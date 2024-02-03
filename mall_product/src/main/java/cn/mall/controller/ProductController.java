package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.*;
import cn.mall.service.IProductService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.framework.qual.RequiresQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PutMapping("/increase/{id}")
    @ApiOperation("产品被收藏数+1")
    @PreAuthorize("hasAuthority('user')")
    @Transactional
    public R addcollectednumber(@PathVariable("id") Integer id){
        productService.addcollectednumber(id);
        return R.success();
    }

    @PutMapping("/decrease/{id}")
    @ApiOperation("产品被收藏数-1")
    @PreAuthorize("hasAuthority('user')")
    @Transactional
    public R reducecollectednumber(@PathVariable("id") Integer id){
        productService.reducecollectednumber(id);
        return R.success();
    }

    @GetMapping("{id}")
    @ApiOperation("根据id获取产品信息")
    @Transactional
    public Product getProductById(@PathVariable("id") Integer id){
        return productService.getById(id);
    }

    @GetMapping("/type/{id}")
    @ApiOperation("根据id获取产品类型")
    public String getTypeById(@PathVariable("id") Integer id){
        return productService.getTypeById(id);
    }

    @GetMapping
    @ApiOperation("根据产品id和类型判断该产品是否符合该类型，符合则返回产品信息")
    public Product getProductByProductIdAndType(@RequestBody ProductIdAndType productIdAndType){
        Product product = productService.getProductByProductIdAndType(productIdAndType);
        return product;
    }

    @PostMapping
    @ApiOperation("添加产品")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    @Transactional
    public R addProduct(@RequestBody ProductDetail productDetail){
        productService.addProduct(productDetail);
        return R.success();
    }

//  因为删除产品之后订单就查询不到产品了，所以取消掉这个接口，产品最多只能停售
//    @DeleteMapping("{id}")
//    @ApiOperation("删除产品")
//    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
//    @GlobalTransactional
//    public R deleteProduct(@PathVariable("id") Integer id){
//        productService.deleteProduct(id);
//        return R.success();
//    }

    @GetMapping("/detail/{id}")
    @ApiOperation("根据id获取产品信息（后台管理）（相比getProductById方法多了产品的图片，类型，产品属性以及对应的值，评论）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R getProductDetailById(@PathVariable("id") Integer id){
        ProductDetail1 productDetailById = productService.getProductDetailById(id);
        return R.success(productDetailById);
    }

    @GetMapping("/detail1/{id}")
    @ApiOperation("根据id获取产品信息（前台展示）（相比getProductById方法多了产品的图片，类型，产品属性以及对应的值，评论）")
    public R getProductDetail1ById(@PathVariable("id") Integer id){
        ProductDetail1 productDetail1ById = productService.getProductDetail1ById(id);
        return R.success(productDetail1ById);
    }

    @PutMapping
    @ApiOperation("修改产品信息")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    @GlobalTransactional
    public R updateProduct(@RequestBody ProductDetail2 productDetail2){
        productService.updateProduct(productDetail2);
        return R.success();
    }

    @GetMapping("/list")
    @ApiOperation("显示所有产品（可根据产品名称，品牌，产品状态进行模糊分页查询）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R listProduct(@RequestBody PageRequestParamWoIdPlus pageRequestParamWoIdPlus){
        IPage<Product> productIPage = productService.listProduct(pageRequestParamWoIdPlus);
        return R.success(productIPage);
    }

//    @PostMapping("/check")
//    @ApiOperation("检查库存，将库存为0的产品的状态设为停售，并更新索引库")
//    @Transactional
//    public R checkStock(){
//        productService.checkStock();
//        return R.success();
//    }

    @PutMapping("/change")
    @ApiOperation("扣库存，当库存为0时将产品状态改为停售，并更新索引库")
    @Transactional
    public R change(@RequestBody IdAndAmount idAndAmount){
        productService.change(idAndAmount);
        return R.success();
    }

    @PutMapping("/change1")
    @ApiOperation("增加成交量，并更新索引库")
    @Transactional
    public R change1(@RequestBody IdAndAmount idAndAmount){
        productService.change1(idAndAmount);
        return R.success();
    }

    @PutMapping("/change2")
    @ApiOperation("返还库存")
    @Transactional
    public R change2(@RequestBody IdAndAmount idAndAmount){
        productService.change2(idAndAmount);
        return R.success();
    }

}
