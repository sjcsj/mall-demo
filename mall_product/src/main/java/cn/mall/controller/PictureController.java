package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.MapAndCount1;
import cn.mall.domain.PageRequestParamWoId;
import cn.mall.domain.Picture;
import cn.mall.domain.Pictures;
import cn.mall.service.IPictureService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private IPictureService pictureService;

    @GetMapping
    @ApiOperation("显示所有产品的图片（可根据产品名称进行模糊分页查询）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    @Transactional
    public R getPicture(@RequestBody PageRequestParamWoId paramWoId) {
        MapAndCount1 picture = pictureService.getPicture(paramWoId);
        return R.success(picture);
    }

    @PostMapping
    @ApiOperation("添加图片（可批量）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    @Transactional
    public R addPicture(@RequestBody Pictures pictures) {
        for (Picture picture : pictures.getPictures()) {
            pictureService.save(picture);
        }
        return R.success();
    }

    @DeleteMapping
    @ApiOperation("删除图片（可批量）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    @Transactional
    public R deletePicture(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            pictureService.removeById(id);
        }
        return R.success();
    }


}
