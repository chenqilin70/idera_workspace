package com.kylin.demo;

import com.kylin.demo.enums.DataType;
import com.kylin.demo.util.CatcherRecursiveTask;
import com.kylin.demo.util.ESClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.FileUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class CatcherMain {
    public static void main(String[] args){
        String basePath="E:/tempFile/catcher_temp_dir/";
        DataType[] ts =  DataType.values();
        for(DataType t:ts){
            ESClient esClient=new ESClient(t.getCode());
            ForkJoinPool pool = new ForkJoinPool();
            CatcherRecursiveTask task = new CatcherRecursiveTask(0,(int)esClient.getCount()-1,esClient,basePath+t.getCode()+"/");
            ForkJoinTask<List<File>> result = pool.submit(task);
            try {
                System.out.println(result.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        for(DataType t:ts){
            ESClient esClient=new ESClient(t.getCode());
            esClient.getCreateHive();
        }

    }
    @Test
    public void test(){
       /* DataType[] values = DataType.values();
        for(DataType t:values){
            ESClient c=new ESClient(t.getCode());
            System.out.println(c.getCreateHive());
        }
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "shunyi-office-venus") .put("client.transport.sniff", true)
                .put("client.transport.ignore_cluster_name", true)
                .build();
        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.41", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.42", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.43", 9300));*/

        Map<String, Object> cpwsMap = new ESClient("cpws").getSource(0, 1).get(0);
        TreeMap<String, Object> treeMap = new TreeMap<>(cpwsMap);
        Set<String> keys = treeMap.keySet();
        List<String> val1=new ArrayList<>();
        for(String k:keys){
            val1.add(treeMap.get(k)+"");
        }


        System.out.println(StringUtils.join(val1,","));
        System.out.println(StringUtils.join(treeMap.values(),","));

    }
}
