package com.kylin.demo.forbgt;

import com.kylin.demo.util.ESClient;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class CatcherRecursiveTask4Bgt extends RecursiveTask<List<File>> {
    private static final int THREAD_HOLD = 2000;
    private  final String TEMP_DIR;
    /*private CatcherHttpClient httpClient=new CatcherHttpClient();*/
    private ESClient4Bgt esClient;
    private int start;
    private int end;

    public CatcherRecursiveTask4Bgt(int start, int end, ESClient4Bgt esClient, String path){
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
            /*int size=(end - start) + 1;
            size/4
            esClient.openScroll()*/
            List<Map<String, Object>> sources = esClient.getSource(start, "sdfsdf");

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
                lines=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.add(file);
        }else{
            int middle = (start + end) / 2;
            CatcherRecursiveTask4Bgt left = new CatcherRecursiveTask4Bgt(start,middle,esClient,TEMP_DIR);
            CatcherRecursiveTask4Bgt right = new CatcherRecursiveTask4Bgt(middle+1,end,esClient,TEMP_DIR);
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
