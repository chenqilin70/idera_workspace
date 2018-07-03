package com.kylin.demo.util;

import com.kylin.demo.enums.DataType;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ESClient {
    private  Client client;
    private String dataType;

    public ESClient( String dataType) {
        this.dataType = dataType;
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "shunyi-office-venus") .put("client.transport.sniff", true)
                .put("client.transport.ignore_cluster_name", true)
                .build();
        client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.41", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.42", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.43", 9300));
    }


    public  long  getCount() {
        SearchResponse searchResponse = client.prepareSearch(dataType).setTypes(dataType).setFrom(0).setSize(1).execute().actionGet();
        return  searchResponse.getHits().getTotalHits();
    }
    public  String getCreateHive(){
        SearchResponse searchResponse = client.prepareSearch(dataType).setTypes(dataType).setFrom(0).setSize(1).execute().actionGet();
        StringBuffer sb=new StringBuffer("create table "+dataType+"(\n");
        sb.append(StringUtils.join(new TreeMap<String ,Object>(searchResponse.getHits().getHits()[0].getSource()).keySet(),"  string ,\n").concat("  string)"));
        sb.append("row format delimited\n" +"fields terminated by '\\001';\n") ;
        sb.append("load data local inpath '/home/fahai/catcher_temp_dir/"+dataType+"/*' into table "+dataType+";");

        return  sb.toString();
    }

    public  List<Map<String,Object>> getSource(int start, int size){
        SearchResponse searchResponse = client.prepareSearch(dataType).setTypes(dataType).setFrom(start).setSize(size).execute().actionGet();
        List<Map<String,Object>> list=new ArrayList<>();

        for(SearchHit h:searchResponse.getHits().getHits()){
            list.add(h.getSource());
        }
        if(list.size()==0){
            System.out.println("请求失败   递归一次");
            list=getSource(start,size);
        }
        return list;

    }

    public static void main(String[] args) {
            StringBuffer sb=new StringBuffer("1");
            sb.append("2");
        System.out.println(sb.toString());

    }


}
