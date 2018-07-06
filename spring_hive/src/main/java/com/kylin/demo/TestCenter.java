package com.kylin.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientFactory;
import org.springframework.data.hadoop.hive.HiveTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TestCenter {
    private HiveTemplate hiveTemplate;
    private HiveClientFactory hiveClientFactory;
    @Before
    public void before(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("hive-context.xml");
        hiveTemplate= (HiveTemplate) ac.getBean("hiveTemplate");
        hiveClientFactory=ac.getBean(HiveClientFactory.class);


    }
    @Test
    public void test() throws SQLException {
        HiveClient hiveClient = hiveClientFactory.getHiveClient();
        hiveClient.execute("use fahai");
//        List<String> query = hiveTemplate.query("select sorttime from cpws limit 1");
        PreparedStatement preparedStatement = hiveClient.getConnection().prepareStatement("select * from cpws limit 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(true){
            boolean next = resultSet.next();
            if(next){
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
                System.out.println(resultSet.getString(3));
                System.out.println(resultSet.getString(4));
                System.out.println(resultSet.getString(5));
                System.out.println(resultSet.getString(6));
                System.out.println(resultSet.getString(7));

            }else{
                break;
            }
        }



    }
    @After
    public void after(){

    }
}
