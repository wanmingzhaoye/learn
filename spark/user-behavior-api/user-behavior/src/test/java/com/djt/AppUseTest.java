package com.djt;

import com.djt.model.SingleAppUseInfo;
import com.djt.security.signature.AESSignature;
import com.djt.utils.JSONUtil;
import com.djt.utils.SignUtils;
import com.djt.vo.AppUseInfo;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppUseTest
{
  @Test
  public void appUseInfo()
  {
    String url = "http://localhost:8080/behavior/gather";
    System.out.println("junit request url==================" + url);
    PostMethod method = new PostMethod(url);

    SingleAppUseInfo appUse1 = new SingleAppUseInfo();
    appUse1.setPackageName("com.qq");
    appUse1.setActiveTime(60000L);

    SingleAppUseInfo appUse2 = new SingleAppUseInfo();
    appUse2.setPackageName("com.wechat");
    appUse2.setActiveTime(120000L);

    List<SingleAppUseInfo> singleAppUseInfoList = new ArrayList();
    singleAppUseInfoList.add(appUse1);
    singleAppUseInfoList.add(appUse2);

    AppUseInfo appUseInfo = new AppUseInfo();
    appUseInfo.setUserId(2000);
    appUseInfo.setSingleAppUseInfoList(singleAppUseInfoList);

    try {
      String beginDateStr = "2019-08-18 10:00:00";
      String endDateStr = "2019-08-18 10:10:00";

      Date beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginDateStr);
      Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateStr);

      appUseInfo.setDay(beginDateStr.substring(0,10));
      appUseInfo.setBeginTime(beginDate.getTime());
      appUseInfo.setEndTime(endDate.getTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    try {
      String json = JSONUtil.fromObject(appUseInfo);
      System.out.println("junit request json========" + json);
      FormatJsonUtil.printJson(json);

      // AES加密
      byte[] bytes = AESSignature.encrypt(json, HttpHelper.parivateKey);

      // 格式化成16进制字符串
      String requestData = SignUtils.getFormattedText(bytes);

      method.setRequestBody(requestData);

      HttpHelper.getHttpClient().executeMethod(method);
      String result = HttpHelper.getHttpRequestResult(method);
      System.out.println("junit response json========" + result);
    } catch (HttpException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}