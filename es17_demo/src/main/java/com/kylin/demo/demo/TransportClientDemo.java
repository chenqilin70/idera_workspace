package com.kylin.demo.demo;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class TransportClientDemo {
    private Client client;
    @Before
    public void befor(){
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "shunyi-office-venus") .put("client.transport.sniff", true)
                .put("client.transport.ignore_cluster_name", true)
                .build();
         client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.41", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.42", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.43", 9300));

    }

    @Test
    public void get() throws ExecutionException, InterruptedException {
        System.out.println(client);
        GetResponse response = client.prepareGet("jyyc", "jyyc", "AU18zaZ06aXWqCOGdQHP")
                .execute()
                .get();
        System.out.println(response.getSourceAsString());
    }
    @Test
    public void search(){
        SearchResponse response = client.prepareSearch("satparty_qs", "satparty_chufa")
                .setTypes("satparty_qs", "satparty_chufa")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("pname","装饰"))             // Query
                .setPostFilter(FilterBuilders.rangeFilter("sortTime").lt(1492099200000l))   // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();

        /*SearchResponse response2 = client.prepareSearch().execute().actionGet();*/
        Iterator<SearchHit> it = response.getHits().iterator();
        while(it.hasNext()){
            SearchHit next = it.next();
            System.out.println(next.getSourceAsString());
        }
    }
    @Test
    public void  scrolls(){

    }



    @After
    public void after(){
        client.close();
    }

    public void showResult(AcknowledgedResponse response){
        System.out.println("isAcknowledged:"+response.isAcknowledged());

    }
    public void showResult(IndexResponse response){
        // Index name
        String _index = response.getIndex();
        // Type name
        String _type = response.getType();
        // Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();
        System.out.println("index:"+_index+"  type:"+_type+"  id:"+_id+"  version:"+_version);
    }
    public void showResult(DeleteResponse response){
        // Index name
        String _index = response.getIndex();
        // Type name
        String _type = response.getType();
        // Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();

        System.out.println("index:"+_index+"  type:"+_type+"  id:"+_id+"  version:"+_version);
    }



}
