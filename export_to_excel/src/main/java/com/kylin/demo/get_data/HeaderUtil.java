package com.kylin.demo.get_data;

import com.kylin.demo.excel.PropertiesUtil;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class HeaderUtil {
    public static void setHeader(HttpRequest request, String fileName) throws IOException {
        InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader=new BufferedReader(new InputStreamReader(resourceAsStream));
        while(true){
            String line = reader.readLine();
            if(line==null){
                break;
            }
            int index=line.indexOf(":");
            String key=line.substring(0,index);
            String value=line.substring(index+1);
            request.setHeader(new BasicHeader(key,value));
        }

    }

    public static void main(String[] args) {
        HttpGet get=new HttpGet();
        try {
            setHeader(get,"risk_header.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
