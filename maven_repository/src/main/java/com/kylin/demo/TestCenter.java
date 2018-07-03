package com.kylin.demo;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;

import java.util.HashMap;
import java.util.Map;

public class TestCenter {
    public static void main(String[] args) {

    }

    public void before() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("cluster.name", "elasticsearch_wenbronk");
        Settings.Builder settings = Settings.builder().put(map);
        Client client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("www.wenbronk.com"), Integer.parseInt("9300")));
    }
}
