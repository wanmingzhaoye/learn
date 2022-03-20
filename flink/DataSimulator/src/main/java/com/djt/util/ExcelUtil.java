package com.djt.util;

import com.djt.entity.DistinctCode;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * excel文件读取工具类
 * @author bigdata
 * @create 2021-08-23-21:13
 */
public class ExcelUtil {
    public static void main(String[] args) {
        getAllByExcel("D:\\data\\china.xls");
    }
    public static List<DistinctCode> getAllByExcel(String path){
        List<DistinctCode> list=new ArrayList<DistinctCode>();

        try {
            Workbook rwb= Workbook.getWorkbook(new File(path));

            Sheet rs=rwb.getSheet("Sheet1");

            int clos=rs.getColumns();//得到所有的列

            int rows=rs.getRows();//得到所有的行

            System.out.println(clos+" rows:"+rows);

            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < clos; j++) {
                    String id=rs.getCell(j++, i).getContents();//默认最左边编号也算一列 所以这里得j++

                    String province=rs.getCell(j++, i).getContents();

                    String provinceCode=rs.getCell(j++, i).getContents();

                    String city=rs.getCell(j++, i).getContents();

                    String cityCode=rs.getCell(j++, i).getContents();

                    String gdp = rs.getCell(j++, i).getContents();

                    System.out.println("id:"+id+" province:"+province+" provinceCode:"+provinceCode
                            +" city:"+city+" cityCode:"+cityCode+" gdp:"+gdp);

                    list.add(new DistinctCode(Integer.parseInt(id), province, provinceCode,city,cityCode, Integer.parseInt(gdp)));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
