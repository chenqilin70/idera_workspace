package com.kylin.demo.forbgt;

import com.kylin.demo.enums.DataType;
import com.kylin.demo.util.CatcherRecursiveTask;
import com.kylin.demo.util.ESClient;
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
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.regex.Pattern;

public class Main4Bgt {
    public static Client client;
    public static String datatype,splitChar;
    private static List<String> cols=null;

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
    static{
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "shunyi-office-venus") .put("client.transport.sniff", true)
                .put("client.transport.ignore_cluster_name", true)
                .build();
        client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.41", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.42", 9300))
                .addTransportAddress(new InetSocketTransportAddress("192.168.6.43", 9300));
         datatype="cpws";
         splitChar="\001";

        SearchResponse searchResponse = client.prepareSearch(datatype).setTypes(datatype).setFrom(0).setSize(1).execute().actionGet();
        cols=new ArrayList<>(searchResponse.getHits().getHits()[0].getSource().keySet());
        System.out.println(cols);

    }


    public static void main(String[] args) {

        SearchResponse scr1 = client.prepareSearch(datatype).setSearchType(SearchType.SCAN)
                .setSize(1000).setScroll(new TimeValue(20000)).execute()
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

            StringBuffer lines=new StringBuffer("");
            for(SearchHit h:hits){
                Map<String, Object> source = h.getSource();
                List<String> valList=new ArrayList<>();
                for(String c:cols){
                    String v = (source.get(c)==null?"":source.get(c)).toString();
                    valList.add(v.replaceAll("\n","").replaceAll(splitChar,"").replaceAll("\r",""));

                }
                System.out.println(StringUtils.join(valList, splitChar).replaceAll("\n","").replaceAll("\r","").concat("\n"));
                lines.append(StringUtils.join(valList, splitChar).replaceAll("\n","").replaceAll("\r","").concat("\n"));
            }
            System.out.println("生成文件中--------");
            File dir=new File("E:/tempFile/catcher_temp_dir/"+datatype+"/");
            if(!dir.exists()){
                dir.mkdirs();
            }
            File file=new File(dir.getAbsolutePath()+"/"+UUID.randomUUID().toString().concat(".data"));
            try {
                file.createNewFile();
                OutputStream os=new FileOutputStream(file,true);
                os.write(lines.substring(0,lines.length()-1).getBytes("utf-8"));
                os.flush();
                os.close();
                lines=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        System.out.println(count);

        StringBuffer sb=new StringBuffer("create table "+datatype+"(\n");
        sb.append(StringUtils.join(cols,"  string ,\n").concat("  string)"));
        sb.append("row format delimited\n" +"fields terminated by '\\001';\n") ;
        sb.append("load data local inpath '/home/fahai/catcher_temp_dir/"+datatype+"/*' into table "+datatype+";");
        System.out.println(sb.toString());


    }

    @Test
    public void test() {
        String s="婚纠纷一审民事裁定书\n" +
                "    返回网站首页 登录\n 注册";
        System.out.println(s.replaceAll("\n",""));
    }
}






























