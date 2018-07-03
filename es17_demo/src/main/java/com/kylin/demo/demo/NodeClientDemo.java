package com.kylin.demo.demo;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.rest.RestStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NodeClientDemo {
    private Node node;
    private Client client;
    @Before
    public void befor(){
        node  =  NodeBuilder.nodeBuilder().settings(ImmutableSettings.settingsBuilder().put("http.enabled", false))
                .client(true).node();
        client=node.client();

    }
    @Test
    public void createIndex(){
        CreateIndexResponse accounts = client.admin().indices().prepareCreate("animal_node").get();
        showResult(accounts);
    }
    @Test
    public void test(){
        GetResponse response = client.prepareGet("jyyc", "jyyc", "jyyc1039819")
                .execute()
                .actionGet();
        System.out.println(response.getSourceAsString());
    }
    @After
    public void after(){
        node.close();
    }
    public void showResult(AcknowledgedResponse response){
        System.out.println("isAcknowledged:"+response.isAcknowledged());

    }
}
