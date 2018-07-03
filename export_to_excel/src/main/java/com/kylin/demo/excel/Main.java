package com.kylin.demo.excel;

import com.alibaba.fastjson.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

public class Main {
    private static Properties properties=PropertiesUtil.getPro("judicature_title.properties");



    public static void main(String[] args) throws IOException {
        JudicatureWorkbook workbook=new JudicatureWorkbook();
        for(Object typeObj:properties.keySet()){
            String type=typeObj.toString();
            JudicatureHttpClient httpClient=new JudicatureHttpClient(type);
            JSONArray arr = httpClient.getJsonObj().getJSONArray(type.concat("List"));
            workbook.addData(arr,type);
        }

        workbook.decorate();
        workbook.create("E:\\tempFile\\result.xls");


    }
    @Test
    public void testExcel() throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet aaa = hssfWorkbook.createSheet("aaa");
        HSSFRow row = aaa.createRow(0);
        System.out.println(row.getLastCellNum());


    }



}
