package com.kylin.demo.forbgt;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ESClient4Bgt {
    private  Client client;
    private String dataType;

    public ESClient4Bgt(String dataType) {
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

    public  List<Map<String,Object>> getSource( int size,String scrollId){
        SearchResponse searchResponse = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(20000)).execute().actionGet();

        List<Map<String,Object>> list=new ArrayList<>();

        for(SearchHit h:searchResponse.getHits().getHits()){
            list.add(h.getSource());
        }
        if(list.size()==0){
            System.out.println("请求失败   递归一次");
            list=getSource(size,scrollId);
        }
        return list;

    }
    public  String openScroll( int size){
        SearchResponse searchResponse = client
                .prepareSearch(dataType).setSearchType(SearchType.SCAN)
                .setSize(size).setScroll(new TimeValue(20000)).execute()
                .actionGet();
        return searchResponse.getScrollId();

    }


    public static void main(String[] args) {
            StringBuffer sb=new StringBuffer("1");
            sb.append("2");
        System.out.println(sb.toString());

    }


}
