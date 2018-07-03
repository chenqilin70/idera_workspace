package com.kylin.demo;


import java.sql.*;

public class HiveDemo2 {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn= DriverManager.getConnection("jdbc:hive2://192.168.6.50:10000/fahai","fahai","fahai!@#456");
        Statement statement = conn.createStatement();
        boolean execute = statement.execute("select * from kylin_test");
        if(execute){
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                int anInt = resultSet.getInt(1);
                System.out.println(anInt);
            }
        }
    }
}
