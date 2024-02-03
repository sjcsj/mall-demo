package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.Evaluation;
import cn.mall.service.IEvaluationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

    @Autowired
    private IEvaluationService evaluationService;

    @GetMapping("{productId}")
    @ApiOperation("根据商品id查询评论，并按时间进行排序")
    public List<Evaluation> getEvaluation(@PathVariable("productId") Integer productId) {
        List<Evaluation> evaluation = evaluationService.getEvaluation(productId);
        return evaluation;
    }

    @DeleteMapping("{productId}")
    @ApiOperation("根据产品id删除评论")
    @Transactional
    public R deleteByProductId(@PathVariable("productId") Integer productId){
        evaluationService.deleteByProductId(productId);
        return R.success();
    }

    @PostMapping("/add")
    @ApiOperation("添加评论")
    @Transactional
    public R addEvaluation(@RequestBody Evaluation evaluation){
        evaluationService.save(evaluation);
        return R.success();
    }


}
