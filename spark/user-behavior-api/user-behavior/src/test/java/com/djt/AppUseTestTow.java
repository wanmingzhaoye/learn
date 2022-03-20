package com.djt;

import com.djt.model.SingleAppUseInfo;
import com.djt.security.signature.AESSignature;
import com.djt.utils.JSONUtil;
import com.djt.utils.SignUtils;
import com.djt.vo.AppUseInfo;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.hadoop.hbase.io.crypto.aes.AES;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模拟手机发送客户端数据
 *
 * @author LiXuekai on 11/1/2021
 */
public class AppUseTestTow {
   @Test
    public void appUseInfo()
   {
       //服务器地址
       String url ="http://locahost:8080/user_behavior_war_exploded/gather";
       //发送post请求
       PostMethod method = new PostMethod(url);

       SingleAppUseInfo appUse1 = new SingleAppUseInfo();
       appUse1.setPackageName("com.qq");
       appUse1.setActiveTime(60000L);

       SingleAppUseInfo appUse2=new SingleAppUseInfo();
       appUse2.setPackageName("com.wx");
       appUse2.setActiveTime(120000L);
       List<SingleAppUseInfo> list =new ArrayList<>();
       list.add(appUse1);
       list.add(appUse2);

       AppUseInfo appUseInfo = new AppUseInfo();
       appUseInfo.setUserId(9527);
       appUseInfo.setSingleAppUseInfoList(list);

       String beginTimeStr="2021-01-11 18:20:00";
       String endTimeStr="2021-01-11 18:30:00";

      try{
          Date beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginTimeStr);
          Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeStr);
          appUseInfo.setBeginTime(beginTime.getTime());
          appUseInfo.setEndTime(endTime.getTime());

          appUseInfo.setDay(beginTime.toString().substring(0,10));
      }
      catch (ParseException e)
      {
          e.printStackTrace();
      }
     try {
         String json = JSONUtil.fromObject(appUseInfo);
         //AES加密
         byte[] bytes= AESSignature.encrypt(json,HttpHelper.parivateKey);

         //byte数组需要装换为16进制字符串
         String requestData = SignUtils.getFormattedText(bytes);
         //发送post请求携带json数据
         method.setRequestBody(requestData);
         //发送post请求
         HttpHelper.getHttpClient().executeMethod(method);
         //请求返回结果
         String  result  =HttpHelper.getHttpRequestResult(method);
         System.out.println("返回结果："+result);

     }catch (HttpException e)
     {
         e.printStackTrace();
     }catch (IOException e)
     {
         e.printStackTrace();
     }

     }
   }

