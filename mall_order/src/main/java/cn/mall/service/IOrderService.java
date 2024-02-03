package cn.mall.service;

import cn.mall.domain.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Map;

public interface IOrderService extends IService<Order> {
    void createOrder(ShoppingCart shoppingCart);

    OrdersAndCountAndTotalprice showNotSubmit(Integer userId);

    void deleteNotSubmit(Integer userId);

    void submitOrder(Integer userId);

    Order getOrderById(Integer id);

    void updateById1(OrderIdAndAmount orderIdAndAmount);

    void updateStatusTo2(List<Integer> ids);

    OrdersAndTotal getOrderList(StatusAndPage statusAndPage);

    void delivery(Integer id);

    void confirm(Integer id);

    Map<String, Integer> getAmount(Integer userId);

    List<Order> getOrder1(Integer userId);

    List<Order> getOrder2(Integer userId);

    List<Order> getOrder3(Integer userId);

    List<Order> getOrder4(Integer userId);

    Integer deleteOrder(Integer id);

    void evaluate(Order order);

    List<Order> getOrder(Integer userId);

    List<Order> getOrder5(Integer userId);

    void updateById2(Order order);

    void cancelNoPay(Integer id);

}
