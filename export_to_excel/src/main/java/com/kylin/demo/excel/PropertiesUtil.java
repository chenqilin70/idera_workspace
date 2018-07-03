package com.kylin.demo.excel;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties getPro(String fileName){
        Properties properties=null;
        try {

            InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
            properties=new Properties();
            properties.load(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
