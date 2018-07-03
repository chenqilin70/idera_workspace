package com.kylin.demo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

public class HttpUtil {
    private static HttpClient httpClient;
    static{
        httpClient= HttpClients.createDefault();
    }
    public static String  getJsonStr(String url){
        String jsonStr="";
        HttpGet httpGet=new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            jsonStr= EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}
