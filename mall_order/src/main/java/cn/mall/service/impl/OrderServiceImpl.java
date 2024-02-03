package cn.mall.service.impl;

import cn.mall.clients.EvaluationClient;
import cn.mall.clients.UserClient;
import cn.mall.clients.ProductClient;
import cn.mall.common.R;
import cn.mall.domain.*;
import cn.mall.mapper.OrderMapper;
import cn.mall.service.IOrderService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private EvaluationClient evaluationClient;

    @Override
    public void createOrder(ShoppingCart shoppingCart) {
        // 先扣库存
        IdAndAmount idAndAmount = new IdAndAmount(shoppingCart.getProductId(), shoppingCart.getAmount());
        productClient.change(idAndAmount);
        // 创建订单
        Order order = new Order();
        order.setUserId(shoppingCart.getUserId());
        order.setProductId(shoppingCart.getProductId());
        order.setAmount(shoppingCart.getAmount());
        R addressByUserId = userClient.getAddressByUserId(shoppingCart.getUserId());
        List<Address> addressList = (List<Address>) addressByUserId.getData();
        String jsonObject = JSON.toJSONString(addressList.get(0));
        Address address = JSONObject.parseObject(jsonObject, Address.class);
        order.setAddressId(address.getId());
        Product product = productClient.getProductById(shoppingCart.getProductId());
        if (product.getProductstatus() == 0) {
            order.setUnitprice(product.getPrice());
        } else {
            order.setUnitprice(product.getSaleprice());
        }
        order.setTotalprice(order.getUnitprice() * order.getAmount());
        orderMapper.insert1(order);
    }

    @Override
    public OrdersAndCountAndTotalprice showNotSubmit(Integer userId) {
        List<Order> orders = orderMapper.showNotSubmit(userId);
        List<Order> orders1 = getOrderList11(orders);
        OrdersAndCountAndTotalprice ordersAndCountAndTotalprice = new OrdersAndCountAndTotalprice();
        ordersAndCountAndTotalprice.setOrders(orders1);
        ordersAndCountAndTotalprice.setCount(orders1.size());
        double sum = 0;
        for (Order order : orders1) {
            sum = sum + order.getTotalprice();
        }
        ordersAndCountAndTotalprice.setTotalprice(sum);
        return ordersAndCountAndTotalprice;
    }

    @Override
    public void deleteNotSubmit(Integer userId) {
        // 返还库存
        List<Order> orderByUserIdAndStatus = orderMapper.getOrderByUserIdAndStatus(userId);
        for (Order order : orderByUserIdAndStatus) {
            IdAndAmount idAndAmount = new IdAndAmount(order.getProductId(), order.getAmount());
            productClient.change2(idAndAmount);
        }
        // 删除待提交订单
        orderMapper.deleteNotSubmit(userId);
    }

    @Override
    public void submitOrder(Integer userId) {
        List<Order> orders = orderMapper.showNotSubmit(userId);
        for (Order order : orders) {
            order.setCreatetime(new Date());
            order.setStatus(1);
            orderMapper.updateById3(order);
        }
    }

    @Override
    public Order getOrderById(Integer id) {
        Order orderById = orderMapper.getOrderById(id);
        return orderById;
    }

    @Override
    public void updateById1(OrderIdAndAmount orderIdAndAmount) {
        Order order = orderMapper.getOrderById(orderIdAndAmount.getOrderId());
        order.setAmount(orderIdAndAmount.getAmount());
        order.setTotalprice(order.getUnitprice() * order.getAmount());
        orderMapper.updateById1(order);
    }

    @Override
    public void updateStatusTo2(List<Integer> ids) {
        for (Integer id : ids) {
            Order order = orderMapper.getOrderById(id);
            order.setStatus(2);
            order.setPaytime(new Date());
            orderMapper.updateById4(order);
            // 增加成交量，更新索引库
            IdAndAmount idAndAmount = new IdAndAmount(order.getProductId(), order.getAmount());
            productClient.change1(idAndAmount);
        }
    }

    @Override
    public OrdersAndTotal getOrderList(StatusAndPage statusAndPage) {
        if (Objects.isNull(statusAndPage.getPage())) {
            statusAndPage.setPage(1);
        }
        if (Objects.isNull(statusAndPage.getSize())) {
            statusAndPage.setSize(10);
        }
        Integer a = statusAndPage.getSize() * (statusAndPage.getPage() - 1);
        Integer b = statusAndPage.getSize();
        OrdersAndTotal ordersAndTotal = new OrdersAndTotal();
        List<Order> orderList = null;
        if (statusAndPage.getStatus() != null) {
            orderList = orderMapper.getOrderList(a, b, statusAndPage.getStatus());
        } else {
            orderList = orderMapper.getOrderList1(a, b);
        }
        List<Order> orderList1 = new ArrayList<>();
        for (Order order : orderList) {
            Product product = productClient.getProductById(order.getProductId());
            R addressById = userClient.getAddressById(order.getAddressId());
            Object data = addressById.getData();
            String jsonObject = JSON.toJSONString(data);
            Address address = JSONObject.parseObject(jsonObject, Address.class);
            order.setProduct(product);
            order.setAddress(address);
            orderList1.add(order);
        }
        ordersAndTotal.setOrders(orderList1);
        ordersAndTotal.setTotal(orderList1.size());
        return ordersAndTotal;
    }

    @Override
    public void delivery(Integer id) {
        Order order = orderMapper.getOrderById(id);
        order.setDeliverytime(new Date());
        order.setStatus(3);
        orderMapper.updateById5(order);
    }

    @Override
    public void confirm(Integer id) {
        Order order = orderMapper.getOrderById(id);
        order.setConfirmtime(new Date());
        order.setStatus(4);
        orderMapper.updateById6(order);
    }

    @Override
    public Map<String, Integer> getAmount(Integer userId) {
        Map<String, Integer> map = new HashMap<>();
        // 待付款
        Integer amount1 = orderMapper.getAmount(userId, 1);
        map.put("待付款", amount1);
        // 待发货
        Integer amount2 = orderMapper.getAmount(userId, 2);
        map.put("待发货", amount2);
        // 待收货
        Integer amount3 = orderMapper.getAmount(userId, 3);
        map.put("待收货", amount3);
        // 待评价
        Integer amount4 = orderMapper.getAmount1(userId);
        map.put("待评价", amount4);
        return map;
    }

    private List<Order> getOrders(Integer userId, Integer status) {
        List<Order> orderList = orderMapper.getOrder(userId, status);
        List<Order> orderList1 = getOrderList11(orderList);
        return orderList1;
    }

    @Override
    public List<Order> getOrder1(Integer userId) {
        List<Order> orders = getOrders(userId, 1);
        return orders;
    }

    @Override
    public List<Order> getOrder2(Integer userId) {
        List<Order> orders = getOrders(userId, 2);
        return orders;
    }

    @Override
    public List<Order> getOrder3(Integer userId) {
        List<Order> orders = getOrders(userId, 3);
        return orders;
    }

    @Override
    public List<Order> getOrder4(Integer userId) {
        List<Order> orders = orderMapper.getOrder1(userId);
        List<Order> orderList1 = getOrderList11(orders);
        return orderList1;
    }

    @Override
    public Integer deleteOrder(Integer id) {
        Order order = orderMapper.getOrderById(id);
        if (order.getStatus() != 4) {
            return 0;
        }
        orderMapper.updateDeleted(id);
        return 1;
    }

    @Override
    public void evaluate(Order order) {
        orderMapper.updateById7(order);
        // 添加评价到评价表
        Evaluation evaluation = new Evaluation();
        evaluation.setProductId(order.getProductId());
        evaluation.setUserId(order.getUserId());
        evaluation.setOrderId(order.getId());
        evaluation.setEvaluation(order.getEvaluation());
        evaluation.setCreatetime(new Date());
        evaluationClient.addEvaluation(evaluation);
    }

    @Override
    public List<Order> getOrder(Integer userId) {
        List<Order> order11 = orderMapper.getOrder11(userId);
        List<Order> orderList = getOrderList11(order11);
        return orderList;
    }

    private List<Order> getOrderList11(List<Order> orders) {
        List<Order> orderList = new ArrayList<>();
        for (Order order : orders) {
            Product productById = productClient.getProductById(order.getProductId());
            order.setProduct(productById);
            R addressById = userClient.getAddressById(order.getAddressId());
            Object data = addressById.getData();
            String jsonObject = JSON.toJSONString(data);
            Address address = JSONObject.parseObject(jsonObject, Address.class);
            order.setAddress(address);
            orderList.add(order);
        }
        return orderList;
    }

    @Override
    public List<Order> getOrder5(Integer userId) {
        List<Order> order2 = orderMapper.getOrder2(userId);
        List<Order> orderList = getOrderList11(order2);
        return orderList;
    }

    @Override
    public void updateById2(Order order) {
        orderMapper.updateById2(order);
    }

    @Override
    public void cancelNoPay(Integer id) {
        // 返还库存
        Order order = orderMapper.getOrderById(id);
        IdAndAmount idAndAmount = new IdAndAmount(order.getProductId(), order.getAmount());
        productClient.change2(idAndAmount);
        // 取消未付款订单
        orderMapper.deleteNoPay(id);
    }


}
