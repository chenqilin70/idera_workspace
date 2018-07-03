package com.kylin.demo.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

public class JudicatureWorkbook  {
    private Workbook wb;
    private Properties pro=PropertiesUtil.getPro("judicature_title.properties");

    public JudicatureWorkbook() {
        
        wb=new HSSFWorkbook();
    }

    public void create(String path){
        File file=new File(path);
        file.deleteOnExit();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            wb.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addData(JSONArray arr, String type) {
        String sheetName = pro.getProperty(type);
        Sheet sheet = getOrCreateSheet(sheetName);
        Row row0=getOrCreateRow(sheet,0);

        for(int i=0 ; i<arr.size();i++){
            JSONObject jsonObject = arr.getJSONObject(i);
            Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                for(Object colObj:jsonObject.keySet()){
                    String colStr=colObj.toString();
                    String data = jsonObject.getString(colStr);
                    if(i==0){
                        Cell colCell = row0.createCell(row0.getLastCellNum()==-1?0:row0.getLastCellNum());
                        colCell.setCellValue(colStr);
                    }
                    Cell cellByContent = getCellByContent(row0, colStr);
                    CellAddress cellAddress = new CellAddress(dataRow.getRowNum(), cellByContent.getColumnIndex());
                    dataRow.createCell(cellByContent.getColumnIndex()).setCellValue(data);

                }

        }

    }
    public Cell getCellByContent(Row row,String content){
        Cell cell=null;
        Iterator<Cell> it = row.cellIterator();
        while(it.hasNext()){
            Cell next=it.next();
            String val = next.getStringCellValue();
            if(val.equals(content)){
                cell=next;
            }
        }
        if(cell==null){
            cell=row.createCell(row.getLastCellNum()+1);
            cell.setCellValue(content);
        }
        return cell;

    }
    public Sheet getOrCreateSheet(String sheetName){
        Sheet sheet = wb.getSheet(sheetName);
        if(sheet==null){
            sheet = wb.createSheet(sheetName);
        }
        return sheet;
    }
    public Row getOrCreateRow(Sheet sheet,int index){
        Row row = sheet.getRow(index);
        if(row==null){
            row=sheet.createRow(index);
        }
        return row;
    }


    public void decorate() {
        int numberOfSheets = wb.getNumberOfSheets();
        for(int index=0;index<numberOfSheets;index++){
            Sheet sheet = wb.getSheetAt(index);
            //整体向下移动一行
            sheet.shiftRows(0, sheet.getLastRowNum(), 1,true,false);
            //添加Title
            Row row0 = sheet.createRow(0);
            Row row1 = sheet.getRow(1);
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,row1.getLastCellNum()-1));
            row0.createCell(0).setCellValue(sheet.getSheetName());
            //标题样式
            CellStyle titleStyle = getTitleStyle();
            Iterator<Cell> iterator=row0.cellIterator();
            while(iterator.hasNext()){
                Cell next = iterator.next();
                next.setCellStyle(titleStyle);
            }
            //列名样式
            Iterator<Cell> it = row1.cellIterator();
            CellStyle colStyle= getColStyle();
            while(it.hasNext()){
                Cell next = it.next();
                next.setCellStyle(colStyle);
            }
            //数据样式
            CellStyle dataStyle = getDataStyle();
            for(int i=2;i<=sheet.getLastRowNum();i++){
                Row row = sheet.getRow(i);
                Iterator<Cell> dataiterator = row.cellIterator();
                while(dataiterator.hasNext()){
                    Cell next = dataiterator.next();
                    next.setCellStyle(dataStyle);
                }
            }
        }

    }
    public CellStyle getTitleStyle(){
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font songFont = wb.createFont();
        songFont.setFontName("宋体");
        songFont.setFontHeight((short)280);
        songFont.setBold(true);
        titleStyle.setFont(songFont);
        titleStyle.setBorderTop(BorderStyle.THICK);
        titleStyle.setBorderLeft(BorderStyle.THICK);
        titleStyle.setBorderRight(BorderStyle.THICK);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        return titleStyle;
    }
    public CellStyle getColStyle(){
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font songFont = wb.createFont();
        songFont.setFontName("宋体");
        songFont.setFontHeight((short)230);
        songFont.setBold(true);
        style.setFont(songFont);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }
    public CellStyle getDataStyle(){
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font songFont = wb.createFont();
        songFont.setFontName("宋体");
        songFont.setFontHeight((short)230);
        songFont.setBold(false);
        style.setFont(songFont);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }
}
