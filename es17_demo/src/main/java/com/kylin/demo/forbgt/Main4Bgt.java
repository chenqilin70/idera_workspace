package com.kylin.demo.forbgt;

import com.kylin.demo.enums.DataType;
import com.kylin.demo.util.CatcherRecursiveTask;
import com.kylin.demo.util.ESClient;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class Main4Bgt {

    /*public static void main(String[] args){
        String basePath="E:/tempFile/catcher_temp_dir/";
        DataType[] ts =  new DataType[]{DataType.BGT};
        for(DataType t:ts){
            ESClient4Bgt esClient=new ESClient4Bgt(t.getCode());
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

    }*/


    public static void main(String[] args) {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "shunyi-office-venus") .put("client.transport.sniff", true)
                .put("client.transport.ignore_cluster_name", true)
                .build();
        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.41", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.42", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.43", 9300));

        SearchResponse commonGet = client.prepareSearch("bgt").setFrom(0).setSize(10).execute().actionGet();
        System.out.println("commonGet:");
        for(SearchHit h:commonGet.getHits().getHits()){
            System.out.print(h.getSource().get("createTime")+",");
        }
        System.out.println("");

        SearchResponse scr1 = client.prepareSearch("bgt").setSearchType(SearchType.SCAN)
                .setSize(100).setScroll(new TimeValue(20000)).execute()
                .actionGet();
        long total = scr1.getHits().getTotalHits();
        int pages = (int) (total / (5 * 100));
        int count=0;
        String scrollId = scr1.getScrollId();
        for(int i=0;i<=pages;i++){
            SearchResponse scr2 = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(20000)).execute().actionGet();
            SearchHit[] hits = scr2.getHits().getHits();
            System.out.println("running……");
            if(hits==null || hits.length==0){
                break;
            }
            count+=hits.length;
            scrollId=scr2.getScrollId();
            
        }
        System.out.println(count);


    }
}






























