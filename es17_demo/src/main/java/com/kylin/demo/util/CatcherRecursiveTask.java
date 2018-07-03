package com.kylin.demo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kylin.demo.enums.DataType;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class CatcherRecursiveTask extends RecursiveTask<List<File>> {
    private static final int THREAD_HOLD = 2000;
    private  final String TEMP_DIR;
    /*private CatcherHttpClient httpClient=new CatcherHttpClient();*/
    private ESClient esClient;
    private int start;
    private int end;

    public CatcherRecursiveTask(int start, int end, ESClient esClient,String path){
        this.start = start;
        this.end = end;
        this.esClient=esClient;
        this.TEMP_DIR=path;
    }

    @Override
    protected List<File> compute() {
        List<File> fileList=new ArrayList<>();
        //如果任务足够小就计算
        boolean canCompute = (end - start) <= THREAD_HOLD;
        if(canCompute){
            StringBuffer lines=new StringBuffer("");
            List<Map<String, Object>> sources = esClient.getSource(start, (end - start) + 1);

            for(Map<String,Object> m:sources){
                TreeMap<String, Object> t = new TreeMap<>(m);
                Collection values=t.values();
                List<String> valList=new ArrayList<>();
                for(Object o:values){
                    String s = o+"";
                    valList.add(s.replaceAll("\r","").replaceAll("\n","").replaceAll("\001","\002"));
                }

                System.out.println(StringUtils.join(valList, "\001").concat("\n"));
                lines.append(StringUtils.join(valList, "\001").concat("\n"));
            }
            System.out.println("生成文件中--------");
            File dir=new File(TEMP_DIR);
            if(!dir.exists()){
                dir.mkdirs();
            }
            File file=new File(TEMP_DIR+UUID.randomUUID().toString().concat(".data"));
            try {
                file.createNewFile();
                OutputStream os=new FileOutputStream(file,true);
                os.write(lines.toString().getBytes("utf-8"));
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.add(file);
        }else{
            int middle = (start + end) / 2;
            CatcherRecursiveTask left = new CatcherRecursiveTask(start,middle,esClient,TEMP_DIR);
            CatcherRecursiveTask right = new CatcherRecursiveTask(middle+1,end,esClient,TEMP_DIR);
            //执行子任务
            left.fork();
            right.fork();
            //获取子任务结果
            List<File> lResult = left.join();
            List<File> rResult = right.join();
            lResult.addAll(rResult);
            fileList=lResult;

        }
        return fileList;
    }


}
