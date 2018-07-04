package com.kylin.demo.enums;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public enum DataType {
    CPWS("cpws","裁判文书"  )
    ,ZXGG("zxgg","执行公告"  )
    ,SX("shixin","失信公告"  )
    ,KTGG("ktgg","开庭公告"  )
    ,FYGG("fygg","法院公告"  )
    ,AJLC("ajlc","案件流程")
    ,BGT("bgt","曝光台")
    ,AJCL("anjianparty","案件串联");


    DataType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static void main(String[] args) {
        File file=new File("E:/tempFile/"+UUID.randomUUID().toString().concat(".data"));
        try {
            file.createNewFile();
            OutputStream os=new FileOutputStream(file,true);
            os.write("abcd".getBytes("utf-8"));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
