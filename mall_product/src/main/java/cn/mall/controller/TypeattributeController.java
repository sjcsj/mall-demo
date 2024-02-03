package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.MapAndCount;
import cn.mall.domain.PageRequestParamWoId;
import cn.mall.domain.TypeAttribute;
import cn.mall.service.ITypeService;
import cn.mall.service.ITypeattributeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/typeattribute")
public class TypeattributeController {

    @Autowired
    private ITypeattributeService typeattributeService;

    @GetMapping
    @ApiOperation("显示各种类型都有哪些属性（可根据类型名称进行模糊分页查询）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R show(@RequestBody PageRequestParamWoId paramWoId) {
        MapAndCount show = typeattributeService.show(paramWoId);
        return R.success(show);
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除某个属性")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    @Transactional
    public R deleteTypeAttribute(@PathVariable("id") Integer id){
        Integer integer = typeattributeService.deleteTypeAttribute(id);
        if (integer == 0){
            return R.error("存在产品使用该属性，无法删除");
        }
        return R.success("删除成功");
    }

    @PostMapping
    @ApiOperation("给某个类型添加属性")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R addTypeAttribute(@RequestBody TypeAttribute typeAttribute){
        typeattributeService.save(typeAttribute);
        return R.success();
    }

    @PutMapping
    @ApiOperation("修改某个属性")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    @Transactional
    public R updateTypeAttribute(@RequestBody TypeAttribute typeAttribute){
        Integer integer = typeattributeService.updateTypeAttribute(typeAttribute);
        if (integer == 0){
            return R.error("存在产品使用该属性，无法修改");
        }
        return R.success("修改成功");
    }

}
