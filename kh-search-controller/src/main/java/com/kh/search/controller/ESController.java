package com.kh.search.controller;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 所在的包名: com.kh.search.controller
 * 所在的项目名：kh-search
 *
 * @Author:xukh
 * @Description:
 * @Date: Created in 23:57 2018/6/13
 */
@RestController
@RequestMapping("/es")
public class ESController {

    @Autowired
    private TransportClient client;

    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity get(@RequestParam(name = "id") String id){

        if (StringUtils.isBlank(id)){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        GetResponse result = this.client.prepareGet("people", "man", id).get();

        if (!result.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(result.getSource(), HttpStatus.OK);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity add(
            @RequestParam(name="name") String name,
            @RequestParam(name="country") String country,
            @RequestParam(name="age") Integer age,
            @RequestParam(name="date") String date){

        try {
            XContentBuilder content = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("name", name)
                    .field("country", country)
                    .field("age", age)
                    .field("date", date)
                    .endObject();

            IndexResponse response = this.client.prepareIndex("people", "man")
                    .setSource(content)
                    .get();

            return new ResponseEntity(response.getId(),HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/del")
    @ResponseBody
    public ResponseEntity del(@RequestParam(name = "id") String id){

        DeleteResponse result = this.client.prepareDelete("people", "man", id).get();

        return new ResponseEntity(result.getResult().toString(),HttpStatus.OK);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity update(String id,
                                 String country,
                                 Integer age,
                                 String date){
        UpdateRequest update = new UpdateRequest("people", "man", id);

        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();

            builder.field("country",country);
            builder.field("age",age);
            builder.field("date",date);

            builder.endObject();
            update.doc(builder);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            UpdateResponse updateResponse = this.client.update(update).get();

            return new ResponseEntity(updateResponse.status().getStatus(),HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/query")
    @ResponseBody
    public ResponseEntity query(String country,
                                Integer age){

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.must(QueryBuilders.matchQuery("country",country));
        boolQuery.must(QueryBuilders.matchQuery("age",age));

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("word").from(123);

        rangeQuery.to(122);

        boolQuery.filter(rangeQuery);

        SearchRequestBuilder builder = this.client.prepareSearch("people")
                .setTypes("man")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(10);

        SearchResponse response = builder.get();

        List<Object> result = new ArrayList<>();

        for (SearchHit hit: response.getHits()) {
            result.add(hit.getSource());
        }

        return new ResponseEntity(result,HttpStatus.OK);
    }

}
