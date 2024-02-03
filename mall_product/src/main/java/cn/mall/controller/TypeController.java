package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.PageRequestParamWoIdAndAmount;
import cn.mall.domain.Product;
import cn.mall.domain.Type;
import cn.mall.domain.TypeAttribute;
import cn.mall.service.ITypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private ITypeService typeService;

    @GetMapping
    @ApiOperation("查询所有产品类型（模糊分页，且可以根据类型对应的产品数量进行筛选）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R getType(@RequestBody PageRequestParamWoIdAndAmount paramWoId){
        IPage<Type> type = typeService.getType(paramWoId);
        return R.success(type);
    }

    @GetMapping("/list")
    @ApiOperation("返回所有产品类型（管理员在添加产品选择类型时可以使用下拉列表，该下拉列表对应该接口）")
    public R getTypeList(){
        List<Type> list = typeService.list();
        return R.success(list);
    }

    @PostMapping
    @ApiOperation("添加产品类型")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R addType(@RequestBody Type type){
        type.setAmount(0);
        typeService.save(type);
        return R.success();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除产品类型")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R deleteType(@PathVariable("id") Integer id){
        Integer integer = typeService.deleteType(id);
        if (integer == 0){
            return R.error("存在产品属于该类型，无法删除");
        }
        return R.success("删除成功");
    }

    @GetMapping("{id}")
    @ApiOperation("根据类型id查询该类型有哪些属性值")
    public R getTypeattributeByTypeId(@PathVariable("id") Integer id){
        List<TypeAttribute> typeattributeByTypeId = typeService.getTypeattributeByTypeId(id);
        return R.success(typeattributeByTypeId);
    }

    @GetMapping("/products/{id}")
    @ApiOperation("根据类型id获取属于该类型的产品")
    public R getProductByTypeId(@PathVariable("id") Integer id){
        List<Product> productByTypeId = typeService.getProductByTypeId(id);
        return R.success(productByTypeId);
    }

    @GetMapping("/typename/{id}")
    @ApiOperation("根据id获取类型名称")
    public String getTypeName(@PathVariable("id") Integer id){
        String typeName = typeService.getTypeName(id);
        return typeName;
    }

}
