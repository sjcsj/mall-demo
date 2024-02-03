package cn.mall.mapper;


import cn.mall.domain.Evaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface EvaluationMapper extends BaseMapper<Evaluation> {
    List<Evaluation> getEvaluation(Integer productId);

    void deleteByProductId(Integer productId);


}
