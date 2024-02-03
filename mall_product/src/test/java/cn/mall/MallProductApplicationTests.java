package cn.mall;

import cn.mall.domain.Product;
import cn.mall.domain.Product1;
import cn.mall.domain.Type;
import cn.mall.service.IProductService;
import cn.mall.service.ITypeService;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class MallProductApplicationTests {

    @Autowired
    private IProductService productService;

    @Autowired
    private ITypeService typeService;

    @Autowired
    private RestHighLevelClient client;

    @Test
    void importdata() throws IOException {
        List<Product> products = productService.list();
        BulkRequest request = new BulkRequest();
        for (Product product : products) {
            // 若状态为停售，则不加入索引库
            if (product.getProductstatus() == 2){
                continue;
            }
            Product1 product1 = new Product1(product);
            Type byId = typeService.getById(product.getTypeId());
            product1.setType(byId.getType());
            product1.addsuggestion();
            request.add(new IndexRequest("mall")
                    .id(product1.getId().toString())
                    .source(JSON.toJSONString(product1), XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }

}
