package com.kylin;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import java.util.Date;

public class SeasonUDF extends UDF {

    public String evaluate(String t){
        String tstr=t.toString();
        String result="";
        if(t==null || StringUtils.isBlank(tstr) || tstr.equals("null")){
            result= "不明";
        }else{
            Date d=new Date(Long.parseLong(tstr));
            int m=d.getMonth()+1;
            if(m<=3){
                result="第一季度";
            }else if(m<=6){
                result="第二季度";
            }else if(m<=9){
                result="第三季度";
            }else{
                result="第四季度";
            }

        }
        System.out.println(t);
        return  result;
    }
}
