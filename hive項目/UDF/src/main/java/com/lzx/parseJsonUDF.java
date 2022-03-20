package com.lzx;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class parseJsonUDF extends UDF
{
   public String evaluate(String line,String key)
   {
       JSONObject json =new JSONObject(line.trim());
       if (json.has(key))
       {
           return json.getString(key);
       }
       return "null";
   }

  /*  public static void main(String[] args) {
        String line = "{\"userId\":9527,\"day\":\"2020-07-18\",\"begintime\":1595058161312,\"endtime\":1595058469010,\"data\":[{\"package\":\"com.browser\",\"activetime\":120000}]}";
        String userId  = new parseJsonUDF().evaluate(line,"begintime");
        System.out.println(userId);
    }*/

}


