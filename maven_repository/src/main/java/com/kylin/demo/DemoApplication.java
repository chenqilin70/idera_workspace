package com.kylin.demo;


import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.hadoop.hive.HiveTemplate;

import java.util.List;

public class DemoApplication {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        HiveTemplate hiveTemplate = (HiveTemplate) ac.getBean("hiveTemplate");
        List list = hiveTemplate.query("show tables;");
        System.out.println(JSON.toJSONString(list));
    }
}
