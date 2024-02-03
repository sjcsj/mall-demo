package cn.mall.clients;

import cn.mall.common.R;
import cn.mall.domain.Evaluation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("mall-evaluation")
@Component
public interface EvaluationClient {

    @GetMapping("/evaluation/{productId}")
    List<Evaluation> getEvaluation(@PathVariable("productId") Integer productId);

    @DeleteMapping("/evaluation/{productId}")
    R deleteByProductId(@PathVariable("productId") Integer productId);

    @PostMapping("/evaluation/add")
    R addEvaluation(@RequestBody Evaluation evaluation);
}
