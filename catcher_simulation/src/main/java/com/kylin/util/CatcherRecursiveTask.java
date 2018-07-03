package com.kylin.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kylin.enums.DataType;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.RecursiveTask;

public class CatcherRecursiveTask extends RecursiveTask<List<File>> {
    private static final int THREAD_HOLD = 50;
    private static final String TEMP_DIR="E:/tempFile/catcher_temp_dir/";
    private CatcherHttpClient httpClient=new CatcherHttpClient();

    private int start;
    private int end;

    public CatcherRecursiveTask(int start,int end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected List<File> compute() {
        List<File> fileList=new ArrayList<>();
        //如果任务足够小就计算
        boolean canCompute = (end - start) <= THREAD_HOLD;
        if(canCompute){
            String lines="";
            for(int i=start;i<=end;i++){
                try {
                    JSONObject jsonObj = httpClient.getJsonObj(DataType.CPWS, i);
                    if(jsonObj==null){
                        System.out.println("未获取到数据");
                        continue;
                    }
                    JSONArray arr = jsonObj.getJSONArray(DataType.CPWS.getCode().concat("List"));

                    if(arr==null){
                        System.out.println("未获取到数据");
                        continue;
                    }
                    System.out.println("获取到数据");
                    for(int j=0;j<arr.size();j++){
                        JSONObject c = (JSONObject) arr.get(j);
                        String[] fields=new String[10];
                        fields[0]=c.getString("cym");
                        fields[1]=c.getString("sortTime");
                        fields[2]=c.getString("caseType");
                        fields[3]=c.getString("body");
                        fields[4]=c.getString("dataType");
                        fields[5]=c.getString("title");
                        fields[6]=c.getString("sortTimeString");
                        fields[7]=c.getString("cpwsId");
                        fields[8]=c.getString("court");
                        fields[9]=c.getString("caseNo");
                        System.out.print(StringUtils.join(fields, "\100").concat("\n"));
                        lines= lines .concat(StringUtils.join(fields, "\100")).concat("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("生成文件中--------");
            File file=new File(TEMP_DIR+UUID.randomUUID().toString().concat(".data"));
            try {
                file.createNewFile();
                OutputStream os=new FileOutputStream(file,true);
                os.write(lines.getBytes("utf-8"));
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.add(file);
        }else{
            int middle = (start + end) / 2;
            CatcherRecursiveTask left = new CatcherRecursiveTask(start,middle);
            CatcherRecursiveTask right = new CatcherRecursiveTask(middle+1,end);
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
