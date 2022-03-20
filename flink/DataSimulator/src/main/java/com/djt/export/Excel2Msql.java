package com.djt.export;

import com.djt.entity.DistinctCode;
import com.djt.util.DBUtil;
import com.djt.util.ExcelUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * excel 文件数据导入mysql 数据库
 * @author bigdata
 * @create 2021-08-23-20:55
 */
public class Excel2Msql {
    public static void main(String[] args) throws SQLException {

        List<DistinctCode> listExcel= ExcelUtil.getAllByExcel("D:\\data\\china.xls");

        DBUtil db=new DBUtil();

        for (DistinctCode dc : listExcel) {
            String sql="insert into distinctcode (id,province,provinceCode,city,cityCode,gdp) values(?,?,?,?,?,?)";
            db.AddDistinctCode(sql, dc);
            System.out.println("数据导入成功");
        }
    }
}
