package cn.mall.service.impl;

import cn.mall.clients.ProductClient;
import cn.mall.domain.*;
import cn.mall.service.IEsService;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import io.seata.common.util.CollectionUtils;
import org.apache.lucene.util.CollectionUtil;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsServiceImpl implements IEsService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public PageResult search(RequestParams params) {
        try {
            SearchRequest request = new SearchRequest("mall");
            buildBasicQuery(params, request);
            // 是否根据成交量进行排序
            if (params.getSort() == 1) {
                request.source().sort("volume", SortOrder.DESC);
            }
            // 高亮处理
            request.source().highlighter(new HighlightBuilder()
                    .field("productname")
                    .requireFieldMatch(false)
            );
            // 分页
            int page = params.getPage();
            int size = params.getSize();
            request.source().from((page - 1) * size).size(size);

            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            return handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Map<String, List<String>> aggregation(RequestParams params) {
        try {
            SearchRequest request = new SearchRequest("mall");
            buildBasicQuery(params, request);
            request.source().size(0);
            // 聚合
            request.source().aggregation(AggregationBuilders
                    .terms("brandAgg")
                    .field("brand")
                    .size(100)
            );
            request.source().aggregation(AggregationBuilders
                    .terms("typeAgg")
                    .field("type")
                    .size(100)
            );
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            Map<String, List<String>> result = new HashMap<>();
            Aggregations aggregations = response.getAggregations();
            List<String> brandList = getAggByName(aggregations, "brandAgg");
            result.put("品牌", brandList);
            List<String> typeList = getAggByName(aggregations, "typeAgg");
            result.put("类型", typeList);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        try {
            SearchRequest request = new SearchRequest("mall");
            request.source().suggest(new SuggestBuilder().addSuggestion(
                    "suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(10)
            ));
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            Suggest suggest = response.getSuggest();
            CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");
            List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
            List<String> list = new ArrayList<>(options.size());
            for (CompletionSuggestion.Entry.Option option : options) {
                String text = option.getText().toString();
                list.add(text);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Product1 product1) {
        try {
            IndexRequest request = new IndexRequest("mall").id(product1.getId().toString());
            request.source(JSON.toJSONString(product1), XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Product1 product1) {
        try {
            UpdateRequest request = new UpdateRequest("mall", product1.getId().toString());
            request.doc(JSON.toJSONString(product1), XContentType.JSON);
            client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            DeleteRequest request = new DeleteRequest("mall", id.toString());
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PageResult handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        SearchHit[] hits = searchHits.getHits();
        List<Product1> product1s = new ArrayList<>();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            Product1 product1 = JSON.parseObject(json, Product1.class);
            // 获取高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highlightFields)) {
                HighlightField highlightField = highlightFields.get("productname");
                if (highlightField != null) {
                    String name = highlightField.getFragments()[0].string();
                    product1.setProductname(name);
                }
            }
            product1s.add(product1);
        }
        return new PageResult(total, product1s);
    }

    private void buildBasicQuery(RequestParams params, SearchRequest request) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String key = params.getKey();
        // 关键字搜索
        if (Strings.isNullOrEmpty(key)) {
//            request.source().query(QueryBuilders.matchAllQuery());
            boolQuery.must(QueryBuilders.matchAllQuery());
        } else {
//            request.source().query(QueryBuilders.matchQuery("all", key));
            boolQuery.must(QueryBuilders.matchQuery("all",params.getKey()));
        }
        // 品牌条件
        if (!Strings.isNullOrEmpty(params.getBrand())) {
            boolQuery.filter(QueryBuilders.termQuery("brand", params.getBrand()));
        }
        // 类型条件
        if (!Strings.isNullOrEmpty(params.getType())) {
            boolQuery.filter(QueryBuilders.termQuery("type", params.getType()));
        }
        // 价格筛选
        if (params.getMinPrice() != null && params.getMaxPrice() != null) {
            boolQuery.filter(QueryBuilders
                    .rangeQuery("price").gte(params.getMinPrice()).lte(params.getMaxPrice()));
        }
        // 算分控制，将广告放在前位
        FunctionScoreQueryBuilder functionScoreQuery =
                QueryBuilders.functionScoreQuery(
                        boolQuery,
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                        QueryBuilders.termQuery("isad", true),
                                        ScoreFunctionBuilders.weightFactorFunction(100)
                                )
                        }
                );
        request.source().query(functionScoreQuery);
    }

    private List<String> getAggByName(Aggregations aggregations, String aggName) {
        Terms terms = aggregations.get(aggName);
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        List<String> stringList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            stringList.add(key);
        }
        return stringList;
    }

}
