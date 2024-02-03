package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.*;
import cn.mall.service.IOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.fenum.qual.FenumTop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.management.DescriptorKey;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping
    @ApiOperation("创建订单(待提交状态)，立即购买和购物车结算都可以")
    @Transactional
    public R createOrder(@RequestBody ShoppingCart shoppingCart) {
        orderService.createOrder(shoppingCart);
        return R.success();
    }

    @GetMapping("/showNotSubmit/{userId}")
    @ApiOperation("显示待提交订单")
    public R showNotSubmit(@PathVariable("userId") Integer userId) {
        OrdersAndCountAndTotalprice ordersAndCountAndTotalprice = orderService.showNotSubmit(userId);
        return R.success(ordersAndCountAndTotalprice);
    }

    @DeleteMapping("{userId}")
    @ApiOperation("删除待提交订单")
    @GlobalTransactional
    public R deleteNotSubmit(@PathVariable("userId") Integer userId) {
        orderService.deleteNotSubmit(userId);
        return R.success();
    }

    @PutMapping("/updateaddress")
    @ApiOperation("修改待提交订单（修改产品收货地址）")
    public R updateNotSubmit1(@RequestBody Order order) {
        orderService.updateById2(order);
        return R.success();
    }

    @PutMapping("/updateamount")
    @ApiOperation("修改待提交订单（修改产品数量）")
    public R updateNotSubmit2(@RequestBody OrderIdAndAmount orderIdAndAmount){
        orderService.updateById1(orderIdAndAmount);
        return R.success();
    }

    @PostMapping("/submit/{userId}")
    @ApiOperation("提交订单")
    @Transactional
    public R submitOrder(@PathVariable("userId") Integer userId){
        orderService.submitOrder(userId);
        return R.success();
    }

    @PostMapping("/cancelNoPay/{id}")
    @ApiOperation("取消未付款订单")
    public R cancelNoPay(@PathVariable("id") Integer id){
        orderService.cancelNoPay(id);
        return R.success();
    }

    @PutMapping("/updatestatus")
    @ApiOperation("用户付款后将订单状态改为待发货，并添加支付时间，产品成交量增加")
    @GlobalTransactional
    public R updateStatusTo2(@RequestBody List<Integer> ids){
        orderService.updateStatusTo2(ids);
        return R.success();
    }

    @GetMapping("{id}")
    @ApiOperation("根据订单id查询订单信息")
    public R getOrderById(@PathVariable("id") Integer id){
        Order order = orderService.getOrderById(id);
        return R.success(order);
    }

    @GetMapping("/list")
    @ApiOperation("得到订单列表（包括被逻辑删除的订单）（可以根据订单状态进行筛选，分页）")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R getOrderList(@RequestBody StatusAndPage statusAndPage){
        OrdersAndTotal orderList = orderService.getOrderList(statusAndPage);
        return R.success(orderList);
    }

    @PostMapping("/delivery/{id}")
    @ApiOperation("对产品进行发货")
    @PreAuthorize("hasAnyAuthority('admin','superadmin')")
    public R delivery(@PathVariable("id") Integer id){
        orderService.delivery(id);
        return R.success();
    }

    @PostMapping("/confirm/{id}")
    @ApiOperation("用户确认收货")
    public R confirm(@PathVariable("id") Integer id){
        orderService.confirm(id);
        return R.success();
    }

    @GetMapping("/amount/{userId}")
    @ApiOperation("待付款，待发货，待收货，待评价的订单数量")
    public R getAmount(@PathVariable("userId") Integer userId){
        Map<String, Integer> amount = orderService.getAmount(userId);
        return R.success(amount);
    }

    @GetMapping("/order/{userId}")
    @ApiOperation("根据用户id查询全部订单")
    public R getOrder(@PathVariable("userId") Integer userId){
        List<Order> order = orderService.getOrder(userId);
        return R.success(order);
    }

    @GetMapping("/order1/{userId}")
    @ApiOperation("根据用户id查询待付款订单")
    public R getOrder1(@PathVariable("userId") Integer userId){
        List<Order> order1 = orderService.getOrder1(userId);
        return R.success(order1);
    }

    @GetMapping("/order2/{userId}")
    @ApiOperation("根据用户id查询待发货订单")
    public R getOrder2(@PathVariable("userId") Integer userId){
        List<Order> order2 = orderService.getOrder2(userId);
        return R.success(order2);
    }

    @GetMapping("/order3/{userId}")
    @ApiOperation("根据用户id查询待收货订单")
    public R getOrder3(@PathVariable("userId") Integer userId){
        List<Order> order3 = orderService.getOrder3(userId);
        return R.success(order3);
    }

    @GetMapping("/order4/{userId}")
    @ApiOperation("根据用户id查询待评价订单")
    public R getOrder4(@PathVariable("userId") Integer userId){
        List<Order> order4 = orderService.getOrder4(userId);
        return R.success(order4);
    }

    @GetMapping("/order5/{userId}")
    @ApiOperation("根据用户id查询已评价订单")
    public R getOrder5(@PathVariable("userId") Integer userId){
        List<Order> order5 = orderService.getOrder5(userId);
        return R.success(order5);
    }

    @DeleteMapping("/deleteOrder/{id}")
    @ApiOperation("用户删除订单（逻辑删除）（订单为交易成功时才可以删）")
    public R deleteOrder(@PathVariable("id") Integer id){
        Integer integer = orderService.deleteOrder(id);
        if (integer == 0){
            return R.error("交易未完成，无法删除");
        }
        return R.success();
    }

    @PostMapping("/evaluation")
    @ApiOperation("用户评价产品（订单交易成功时才可以评价）")
    @GlobalTransactional
    public R evaluate(@RequestBody Order order){
        orderService.evaluate(order);
        return R.success();
    }

}
