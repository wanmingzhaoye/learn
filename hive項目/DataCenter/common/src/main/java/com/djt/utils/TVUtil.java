package com.djt.utils;
import java.net.URLDecoder;
import java.util.ArrayList;
import com.djt.pojo.TV;
import com.djt.pojo.UserLog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * @author dajiangtai
 * @create 2020-04-09-16:23
 */
public class TVUtil {
    @SuppressWarnings("unchecked")
    public static TV parseTVData(String text) {
        try {
            //通过Jsoup解析每行数据
            Document doc = Jsoup.parse(text);

            //获取WIC标签内容，每行数据只有一个WIC标签
            Elements content = doc.getElementsByTag("WIC");

            //解析出卡号编号
            String cardNum = content.get(0).attr("cardNum");
            if(cardNum == null||"".equals(cardNum)){
                return null;
            }

            //解析出机顶盒号
            String stbNum = content.get(0).attr("stbNum");
            if(stbNum == null||"".equals(stbNum)){
                return null;
            }

            //解析出日期
            String date = content.get(0).attr("date");
            if(date == null||"".equals(date)){
                return null;
            }

            String pageWidgetVersion = content.get(0).attr("pageWidgetVersion");

            TV tv = new TV();
            tv.setCardNum(cardNum);
            tv.setStbNum(stbNum);
            tv.setDate(date);
            tv.setPageWidgetVersion(pageWidgetVersion);

            //解析A标签
            Elements els = doc.getElementsByTag("A");


            ArrayList<UserLog> list = new ArrayList<UserLog>();
            for (Element el : els) {
                //解析结束时间
                String e = el.attr("e");
                if(e ==null||"".equals(e)){
                    break;
                }
                //解析起始时间
                String s = el.attr("s");
                if(s == null||"".equals(s)){
                    break;
                }

                String n = el.attr("n");

                String t = el.attr("t");

                String pi = el.attr("pi");

                //解析节目内容
                String p = el.attr("p");
                if(p == null||"".equals(p)){
                    break;
                }

                //解析频道
                String sn = el.attr("sn");

                if(sn ==null||"".equals(sn)){
                    break ;
                }

                //对节目解码
                p = URLDecoder.decode(p, "utf-8");

                //解析出统一的节目名称，比如：天龙八部(1)，天龙八部(2)，同属于一个节目
                int index = p.indexOf("(");

                if (index != -1) {
                    p = p.substring(0, index);
                }

                UserLog userLog = new UserLog();
                userLog.setE(e);
                userLog.setS(s);
                userLog.setN(n);
                userLog.setT(t);
                userLog.setPi(pi);
                userLog.setP(p);
                userLog.setSn(sn);
                list.add(userLog);
            }

            tv.setList(list);
            return tv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
