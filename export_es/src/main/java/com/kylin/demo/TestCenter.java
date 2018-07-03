package com.kylin.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.master.ShardsAcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.TransportResponse;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class TestCenter {
    private TransportClient client;
    @Before
    public void before() throws UnknownHostException {
        // on startup
        /*Settings settings = Settings.builder()
                .put("cluster.name", "myApp").build();*/
        /*TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9301))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9302))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9303));*/

        Settings settings = Settings.builder()
                .put("cluster.name", "shunyi-office-venus").build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.6.41"), 9300))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.6.41"), 9300))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.6.41"), 9300));;



    }

    @Test
    public void deleteIndex(){
        DeleteIndexResponse accounts = client.admin().indices().prepareDelete("animal").execute().actionGet();
        showResult(accounts);
    }
    @Test
    public void createIndex(){
        CreateIndexResponse accounts = client.admin().indices().prepareCreate("animal").get();
        showResult(accounts);
    }
    @Test
    public void createType(){
        String json="{\n" +
                "    \n" +
                "\t    \"pig\": {\n" +
                "\t      \"properties\": {\n" +
                "\t\t\"color\": {\n" +
                "\t\t  \"analyzer\": \"ik_max_word\",\n" +
                "\t\t  \"type\": \"text\",\n" +
                "\t\t  \"fielddata\":true\n" +
                "\t\t},\n" +
                "\t\t\"name\": {\n" +
                "\t\t  \"analyzer\": \"ik_max_word\",\n" +
                "\t\t  \"type\": \"text\"\n" +
                "\t\t},\n" +
                "\t\t\"native\": {\n" +
                "\t\t  \"analyzer\": \"ik_max_word\",\n" +
                "\t\t  \"type\": \"text\"\n" +
                "\t\t},\n" +
                "\t\t\"age\": {\n" +
                "\t\t  \"type\": \"integer\"\n" +
                "\t\t}\n" +
                "\t    }\n" +
                "\t  }\n" +
                "    \n" +
                "}";
        PutMappingRequest mappingRequest = Requests.putMappingRequest("animal").type("pig").source(json,XContentType.JSON);
        PutMappingResponse putMappingResponse = client.admin().indices().putMapping(mappingRequest).actionGet();
        showResult(putMappingResponse);
    }
    @Test
    public void deleteData(){
        DeleteResponse deleteResponse = client.prepareDelete("animal", "pig", "rd4MSmQB3WwyNTb-ik1l").get();
        showResult(deleteResponse);
    }
    @Test
    public void addData(){
        JSONObject json = new JSONObject();
        json.put("color", "绿色，红色");
        json.put("name", "God");
        json.put("native", "中国山西");
        json.put("age",25);

        JSONObject json2 = new JSONObject();
        json2.put("color", "绿色，灰色");
        json2.put("name", "Rose");
        json2.put("native", "中国上海");
        json2.put("age",18);

        JSONObject json3 = new JSONObject();
        json3.put("color", "红色，灰色");
        json3.put("name", "Kylin");
        json3.put("native", "中国湖南");
        json3.put("age",28);

        JSONObject json4 = new JSONObject();
        json4.put("color", "绿色，黄色");
        json4.put("name", "Lucy");
        json4.put("native", "中国湖南");
        json4.put("age",30);


        JSONObject json5 = new JSONObject();
        json5.put("color", "黑色，灰色");
        json5.put("name", "lina");
        json5.put("native", "中国甘肃");
        json5.put("age",33);



        IndexResponse response = client.prepareIndex("animal", "pig")
                .setSource(json, XContentType.JSON)
                .get();
        /*showResult(response);
         response = client.prepareIndex("animal", "pig")
                .setSource(json2, XContentType.JSON)
                .get();
        showResult(response);
        response = client.prepareIndex("animal", "pig")
                .setSource(json3, XContentType.JSON)
                .get();
        showResult(response);
        response = client.prepareIndex("animal", "pig")
                .setSource(json4, XContentType.JSON)
                .get();
        showResult(response);
        response = client.prepareIndex("animal", "pig")
                .setSource(json5, XContentType.JSON)
                .get();*/
        showResult(response);
    }



    @Test
    public void test(){
        System.out.println(client);

    }
    public void showResult(DocWriteResponse response){
        // Index name
        String _index = response.getIndex();
        // Type name
        String _type = response.getType();
        // Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();
        // status has stored current instance statement.
        RestStatus status = response.status();

        System.out.println("index:"+_index+"  type:"+_type+"  id:"+_id+"  version:"+_version+" status:"+status.toString());

    }
    public void showResult(AcknowledgedResponse response){
        System.out.println("isAcknowledged:"+response.isAcknowledged());

    }



    @After
    public void after(){
    // on shutdown
        client.close();
    }
}
