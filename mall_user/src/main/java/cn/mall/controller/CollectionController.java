package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.*;
import cn.mall.service.ICollectionService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private ICollectionService collectionService;

    @PostMapping
    @ApiOperation("添加收藏，产品被收藏数+1")
    @PreAuthorize("hasAuthority('user')")
    @GlobalTransactional
    @Transactional
    public R add(@RequestBody Collection collection) {
        collectionService.addCollection(collection);
        return R.success("添加成功");
    }

    @DeleteMapping
    @ApiOperation("取消收藏，产品被收藏数-1")
    @PreAuthorize("hasAuthority('user')")
    @GlobalTransactional
    public R deleteCollection(@RequestBody Collection collection) {
        collectionService.removeByUserIdAndProductId(collection);
        return R.success();
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量取消收藏")
    @PreAuthorize("hasAuthority('user')")
    @GlobalTransactional
    public R deleteCollectionByIds(@RequestBody UserIdAndProductIds userIdAndProductIds){
        collectionService.deleteCollectionByIds(userIdAndProductIds);
        return R.success();
    }

    @GetMapping("/count/{id}")
    @ApiOperation("某用户收藏产品的数量")
    @PreAuthorize("hasAuthority('user')")
    public R collectionnumber(@PathVariable("id") Integer id){
        return R.success(collectionService.collectionnumber(id));
    }

    @GetMapping("{id}")
    @ApiOperation("根据用户id查询收藏的产品，并按收藏时间进行排序")
    @PreAuthorize("hasAuthority('user')")
    public R getProductByUserId(@PathVariable("id") Integer id) {
        List<ProductAndCollectiontime> productAndCollectiontimes =
                collectionService.getProductByUserId(id);
        return R.success("获取成功",productAndCollectiontimes);
    }

    @GetMapping("/type/{id}")
    @ApiOperation("返回用户收藏产品的类型")
    @PreAuthorize("hasAuthority('user')")
    public R getTypeByUserId(@PathVariable("id") Integer id){
        Set<String> typeList = collectionService.getTypeByUserId(id);
        return R.success(typeList);
    }

    @GetMapping
    @ApiOperation("根据用户id和产品类型返回用户收藏的产品，并按收藏时间进行排序")
    @PreAuthorize("hasAuthority('user')")
    public R getProductByUserIdAndType(@RequestBody UserIdAndType userIdAndType){
        List<ProductAndCollectiontime> productAndCollectiontimes = collectionService.getProductByUserIdAndType(userIdAndType);
        return R.success(productAndCollectiontimes);
    }

    @PostMapping("/exist")
    @ApiOperation("根据用户id和产品id查看是否存在该收藏记录")
    @Transactional
    public boolean exist(@RequestBody ShoppingCart shoppingCart){
        return collectionService.exist(shoppingCart);
    }

    @DeleteMapping("/deleteByProduct/{productId}")
    @ApiOperation("根据产品id删除收藏记录")
    @Transactional
    public R deleteCollectionByProductId(@PathVariable("productId") Integer productId){
        collectionService.deleteCollectionByProductId(productId);
        return R.success();
    }
}
