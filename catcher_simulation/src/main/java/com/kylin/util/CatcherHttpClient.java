package com.kylin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kylin.enums.DataType;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class CatcherHttpClient {
    private  CloseableHttpClient httpClient;
    private  final String AUTH_CODE="c4o89J2kQOEEQ9PizQ8g" ;

    public CatcherHttpClient() {
        httpClient= HttpClients.createDefault();
    }

    public  JSONObject getJsonObj(DataType dataType,Integer pageno) throws IOException {
        HttpGet get=new HttpGet("http://app.fahaicc.com/fhfk/query.jsp?authCode=".concat(AUTH_CODE)
                .concat("&q=@status:exists&datatype=").concat(dataType.getCode()).concat("&pageno=").concat(pageno.toString()));
        CloseableHttpResponse resp = httpClient.execute(get);
        String jsonStr=EntityUtils.toString(resp.getEntity());
        JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
        return jsonObject;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new CatcherHttpClient().getJsonObj(DataType.CPWS,1));
    }

}
