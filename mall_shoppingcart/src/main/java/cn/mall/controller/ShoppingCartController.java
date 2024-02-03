package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.ShoppingcartsAndTotalprice;
import cn.mall.domain.ShoppingCart;
import cn.mall.domain.UserIdAndKeyword;
import cn.mall.service.IShoppingCartService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @DeleteMapping("{id}")
    @ApiOperation("删除某位用户的购物车")
    @Transactional
    public R deleteByUserId(@PathVariable("id") Integer id) {
        shoppingCartService.deleteByUserId(id);
        return R.success();
    }

    @PostMapping
    @ApiOperation("添加产品到购物车,传参：用户id，产品id，产品数量")
    @PreAuthorize("hasAuthority('user')")
    public R add(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.add(shoppingCart);
        return R.success();
    }

    @GetMapping("{id}")
    @ApiOperation("根据用户id得到购物车里的产品等信息")
    @PreAuthorize("hasAuthority('user')")
    @GlobalTransactional
    public R getInfoByUserId(@PathVariable("id") Integer id) {
        ShoppingcartsAndTotalprice shoppingcartsAndTotalprice = shoppingCartService.getInfoByUserId(id);
        return R.success(shoppingcartsAndTotalprice);
    }

    @GetMapping("/count/{id}")
    @ApiOperation("查询当前用户购物车产品的数量")
    @PreAuthorize("hasAuthority('user')")
    public R getCountByUserId(@PathVariable("id") Integer id) {
        Integer count = shoppingCartService.getCountByUserId(id);
        return R.success(count);
    }

    @GetMapping("/count1/{id}")
    @ApiOperation("已选产品的数量")
    @PreAuthorize("hasAuthority('user')")
    public R getSelectedproductByUserId(@PathVariable("id") Integer id) {
        Integer count = shoppingCartService.getSelectedproductByUserId(id);
        return R.success(count);
    }

    @PutMapping
    @ApiOperation("对某件产品的勾选状态或数量进行更新")
    @PreAuthorize("hasAuthority('user')")
    public R updateById(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.updateById(shoppingCart);
        return R.success();
    }

    @PostMapping("/collection")
    @ApiOperation("将某件产品移入收藏夹")
    @PreAuthorize("hasAuthority('user')")
    @GlobalTransactional
    public R addCollection(@RequestBody ShoppingCart shoppingCart) {
        shoppingCartService.addCollection(shoppingCart);
        return R.success();
    }

    @PostMapping("/collection/{userId}")
    @ApiOperation("将勾选的产品移入收藏夹")
    @PreAuthorize("hasAuthority('user')")
    @GlobalTransactional
    public R addCollections(@PathVariable("userId") Integer userId) {
        Integer integer = shoppingCartService.addCollections(userId);
        if (integer == 0) {
            return R.error("请选择宝贝");
        }
        return R.success();
    }

    @DeleteMapping("/deleteone/{id}")
    @ApiOperation("删除某条购物车信息")
    @PreAuthorize("hasAuthority('user')")
    public R deleteOneInfo(@PathVariable("id") Integer id) {
        shoppingCartService.removeById(id);
        return R.success();
    }

    @DeleteMapping("/delete/{userId}")
    @ApiOperation("删除勾选的产品")
    @PreAuthorize("hasAuthority('user')")
    @Transactional
    public R deleteTick(@PathVariable("userId") Integer userId) {
        Integer integer = shoppingCartService.deleteTick(userId);
        if (integer == 0) {
            return R.error("请选择宝贝");
        }
        return R.success();
    }

    @PutMapping("/updateall/{id}")
    @ApiOperation("给全选复选框打勾")
    @PreAuthorize("hasAuthority('user')")
    public R updateAll(@PathVariable Integer id) {
        shoppingCartService.updateall(id);
        return R.success();
    }

    @PutMapping("/cancelall/{id}")
    @ApiOperation("取消全选复选框的勾")
    @PreAuthorize("hasAuthority('user')")
    public R cancelAll(@PathVariable Integer id) {
        shoppingCartService.cancelall(id);
        return R.success();
    }

    @GetMapping("/judge/{id}")
    @ApiOperation("根据购物车信息的打勾情况判断全选复选框是否打勾")
    @PreAuthorize("hasAuthority('user')")
    public R judge(@PathVariable Integer id) {
        Integer judge = shoppingCartService.judge(id);
        if (judge == 0) {
            return R.success("否");
        }
        return R.success("是");
    }

    @PostMapping("/settlement/{userId}")
    @ApiOperation("结算勾选的产品")
    @PreAuthorize("hasAuthority('user')")
    @GlobalTransactional
    public R settlement(@PathVariable("userId") Integer userId){
        Integer integer = shoppingCartService.settlement(userId);
        if (integer == 0){
            return R.error("请选择宝贝");
        }
        if (integer == 1){
            return R.error("你还未添加收货地址，请先添加收货地址");
        }
        return R.success();
    }

    @DeleteMapping("/byProductId/{productId}")
    @ApiOperation("根据产品id删除购物车记录")
    @Transactional
    public R deleteByProductId(@PathVariable("productId") Integer productId){
        shoppingCartService.deleteByProductId(productId);
        return R.success();
    }


}
