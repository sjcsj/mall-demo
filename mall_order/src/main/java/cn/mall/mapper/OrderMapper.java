package cn.mall.mapper;


import cn.mall.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    List<Order> showNotSubmit(Integer userId);

    void deleteNotSubmit(Integer userId);

    void insert1(Order order);

    Order getOrderById(Integer id);

    List<Order> getOrderList(Integer a, Integer b, Integer status);

    List<Order> getOrderList1(Integer a, Integer b);

    Integer getAmount(Integer userId, Integer status);

    Integer getAmount1(Integer userId);

    List<Order> getOrder(Integer userId, Integer status);

    List<Order> getOrder1(Integer userId);

    List<Order> getOrder11(Integer userId);

    List<Order> getOrder2(Integer userId);

    void updateById2(Order order);

    void updateById1(Order order);

    void updateById3(Order order);

    void updateById4(Order order);

    List<Order> getOrderByUserIdAndStatus(Integer userId);

    void deleteNoPay(Integer id);

    void updateById5(Order order);

    void updateById6(Order order);

    void updateDeleted(Integer id);

    void updateById7(Order order);


}
