package cn.mall.service.impl;

import cn.mall.clients.OrderClient;
import cn.mall.clients.UserClient;
import cn.mall.common.R;
import cn.mall.domain.Evaluation;
import cn.mall.domain.Order;
import cn.mall.domain.User;
import cn.mall.mapper.EvaluationMapper;
import cn.mall.service.IEvaluationService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements IEvaluationService {

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderClient orderClient;

    @Override
    public List<Evaluation> getEvaluation(Integer productId) {
        List<Evaluation> evaluations = evaluationMapper.getEvaluation(productId);
        List<Evaluation> evaluations1 = new ArrayList<>();
        for (Evaluation evaluation : evaluations) {
            R userById = userClient.getUserById(evaluation.getUserId());
            String jsonObject = JSON.toJSONString(userById.getData());
            //将json转成需要的对象
            User user = JSONObject.parseObject(jsonObject, User.class);
            R orderById = orderClient.getOrderById(evaluation.getOrderId());
            String jsonObject1 = JSON.toJSONString(orderById.getData());
            //将json转成需要的对象
            Order order = JSONObject.parseObject(jsonObject1, Order.class);
            evaluation.setUser(user);
            evaluation.setOrder(order);
            evaluations1.add(evaluation);
        }
        return evaluations1;
    }

    @Override
    public void deleteByProductId(Integer productId) {
        evaluationMapper.deleteByProductId(productId);
    }
}
