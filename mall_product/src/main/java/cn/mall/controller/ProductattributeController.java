package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.PageRequestParamWoId;
import cn.mall.domain.ProductAttribute;
import cn.mall.domain.ProductAttributeDetails1;
import cn.mall.service.IProductattributeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productattribute")
public class ProductattributeController {

    @Autowired
    private IProductattributeService productattributeService;

    @GetMapping
    @ApiOperation("显示所有产品的属性以及属性的值（可根据产品名称进行模糊分页查询）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R getProductAttribute(@RequestBody PageRequestParamWoId paramWoId){
        ProductAttributeDetails1 productAttribute = productattributeService.getProductAttribute(paramWoId);
        return R.success(productAttribute);
    }

    @PostMapping
    @ApiOperation("给产品的属性添加属性值（修改产品的属性值）（删除产品的属性值）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R addProductAttribute(@RequestBody ProductAttribute productAttribute){
        productattributeService.updateById(productAttribute);
        return R.success();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除产品的某个属性的值")
    public R deleteProductAttribute(@PathVariable("id") Integer id){
        productattributeService.deleteProductAttribute(id);
        return R.success();
    }

}
