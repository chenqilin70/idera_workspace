package com.kylin.demo.get_data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpsParameters;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RiskHttp {
    private static CloseableHttpClient httpClient;

    static {
        httpClient= HttpClients.createDefault();
    }

    public static JSONObject getJsonObj() throws IOException {
        HttpPost post=new HttpPost("http://banktest.fahaicc.com/search/content");
        HeaderUtil.setHeader(post,"risk_header.txt");

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        NameValuePair nameValuePair=new BasicNameValuePair("queryWord","华为");
        nvps.add(nameValuePair);
        nameValuePair=new BasicNameValuePair("queryType","pname");
        nvps.add(nameValuePair);
        nameValuePair=new BasicNameValuePair("pageNo","1");
        nvps.add(nameValuePair);
        nameValuePair=new BasicNameValuePair("type","all");
        nvps.add(nameValuePair);
        nameValuePair=new BasicNameValuePair("searchType","all");
        nvps.add(nameValuePair);
        nameValuePair=new BasicNameValuePair("eventLevel","all");
        nvps.add(nameValuePair);
        nameValuePair=new BasicNameValuePair("authCode","c4o89J2kQOEEQ9PizQ8g");
        nvps.add(nameValuePair);
        nameValuePair=new BasicNameValuePair("customerSearchSign","80b61dad-8791-685c-b80e-c2550f70abce");
        nvps.add(nameValuePair);
        post.setEntity(new UrlEncodedFormEntity(nvps));

        CloseableHttpResponse resp = httpClient.execute(post);
        String jsonStr=EntityUtils.toString(resp.getEntity());
        System.out.println(jsonStr);
        JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
        return jsonObject;
    }

    public static void main(String[] args) throws IOException {
        getJsonObj();
    }
}
