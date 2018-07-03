package com.kylin.demo.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;

import java.io.IOException;

public class JudicatureHttpClient {
    private  CloseableHttpClient httpClient;
    private  final String AUTH_CODE="c4o89J2kQOEEQ9PizQ8g" ;
    private  String DATA_TYPE="";

    public JudicatureHttpClient(String dataType) {
        DATA_TYPE=dataType;
        httpClient= HttpClients.createDefault();
    }

    public  JSONObject getJsonObj() throws IOException {
        HttpGet get=new HttpGet("http://app.fahaicc.com/fhfk/query.jsp?authCode="+AUTH_CODE+"&q=@status:exists&datatype=".concat(DATA_TYPE));
        CloseableHttpResponse resp = httpClient.execute(get);
        String jsonStr=EntityUtils.toString(resp.getEntity());
        JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
        return jsonObject;
    }

}
