package cn.mall.service;

import cn.mall.domain.Evaluation;
import cn.mall.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IEvaluationService extends IService<Evaluation> {
    List<Evaluation> getEvaluation(Integer productId);

    void deleteByProductId(Integer productId);

}
