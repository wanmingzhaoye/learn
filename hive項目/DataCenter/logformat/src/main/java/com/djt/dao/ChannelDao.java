package com.djt.dao;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dajiangtai
 * @create 2020-04-10-13:55
 */
public class ChannelDao extends AbstractTVDAO {
    private static Logger logger = Logger.getLogger(ChannelDao.class.getName());
    @Override
    public Map<String, String> parseTVObj(Path[] cacheFilesPaths) throws IOException {
        Map<String, String> curNumMap = new HashMap<String, String>();
        BufferedReader br;
        String infoAddr = null;
            for (Path path : cacheFilesPaths) {
                String pathStr = path.toString();
                System.out.println("pathStr="+pathStr);
                br = new BufferedReader(new FileReader(pathStr));
                while (null != (infoAddr = br.readLine())) {
                    System.out.println("infoAddr="+infoAddr);
                    // 按行读取并解析当前在播数据
                    String[] tvjoin = StringUtils.split(infoAddr.toString(),
                            "@");
                    if (tvjoin.length == 3) {
                        curNumMap.put(
                                tvjoin[0].trim() + "@" + tvjoin[1].trim(),
                                tvjoin[2].trim());
                    }
                }
            }
        return curNumMap;
    }
}
