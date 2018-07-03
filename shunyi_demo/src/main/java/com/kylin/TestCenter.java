package com.kylin;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Before;
import org.junit.Test;

public class TestCenter {
    private Client client;
    @Before
    public void befor(){
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "shunyi-office-venus") .put("client.transport.sniff", true)
                .put("client.transport.ignore_cluster_name", true)
                .build();
        client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.41", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.42", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.43", 9300));

    }
    @Test
    public void get(){
        /*System.out.println(client);
        GetResponse response = client.prepareGet("jyyc", "jyyc", "jyyc1039819")
                .execute()
                .actionGet();
        System.out.println(response.getSourceAsString());*/
        System.out.println(client.prepareGet("jyyc","jyyc","jyyc1039819").get());


    }
}
