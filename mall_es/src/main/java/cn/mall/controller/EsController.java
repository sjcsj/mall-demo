package cn.mall.controller;

import cn.mall.common.R;
import cn.mall.domain.PageResult;
import cn.mall.domain.Product;
import cn.mall.domain.RequestParams;
import cn.mall.service.IEsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/es")
public class EsController {

    @Autowired
    private IEsService esService;

    @PostMapping
    @ApiOperation("搜索产品数据")
    public R search(@RequestBody RequestParams requestParams){
        PageResult search = esService.search(requestParams);
        return R.success(search);
    }

    @PostMapping("/data")
    @ApiOperation("数据聚合（对产品的品牌和类型进行聚合")
    public R aggregation(@RequestBody RequestParams params){
        Map<String, List<String>> aggregation = esService.aggregation(params);
        return R.success(aggregation);
    }

    @GetMapping("/suggestion")
    @ApiOperation("自动补全查询")
    public R getSuggestions(@RequestBody String prefix){
        List<String> suggestions = esService.getSuggestions(prefix);
        return R.success(suggestions);
    }

}
