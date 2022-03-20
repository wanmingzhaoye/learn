package com.djt.util;

import com.djt.entity.DistinctCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库连接工具类
 * @author bigdata
 * @create 2021-08-23-21:31
 */
public class DBUtil {
    private static final String connectionURL = "jdbc:mysql://192.168.0.104:3306/live?useUnicode=true&characterEncoding=UTF8&useSSL=false";
    private static final String username = "root";
    private static final String password = "root";
    //创建数据库的连接
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return   DriverManager.getConnection(connectionURL,username,password);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    //关闭数据库的连接
    public static void close(ResultSet rs,Statement stmt,Connection con) throws SQLException {
        if(rs!=null)
            rs.close();
        if(stmt!=null)
            stmt.close();
        if(con!=null)
            con.close();
    }

    /**
     * 获取城市编码集合
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<String> getCityCodeList(String sql) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<>();
        try {
            con=getConnection();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()){
                String cityCode = rs.getString("cityCode");
                list.add(cityCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            close(null,pst,con);
        }
        return list;
    }

    /**
     * 查询地区完整数据
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<DistinctCode> getDCList(String sql) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<DistinctCode> list = new ArrayList<>();
        try {
            con=getConnection();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String province = rs.getString("province");
                String provinceCode = rs.getString("provinceCode");
                String city = rs.getString("city");
                String cityCode = rs.getString("cityCode");
                int gdp = rs.getInt("gdp");
                DistinctCode dc = new DistinctCode(id, province, provinceCode, city, cityCode, gdp);
                list.add(dc);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            close(null,pst,con);
        }
        return list;
    }

    /**
     * 插入地区数据
     * @param sql
     * @param dc
     * @return
     * @throws SQLException
     */
    public int AddDistinctCode(String sql, DistinctCode dc) throws SQLException {
        int a = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con=getConnection();
            pst = con.prepareStatement(sql);
            if (dc != null) {
                pst.setInt(1,dc.getId());
                pst.setString(2,dc.getProvince());
                pst.setString(3,dc.getProvinceCode());
                pst.setString(4,dc.getCity());
                pst.setString(5,dc.getCityCode());
                pst.setInt(6,dc.getGdp());
            }
            a = pst.executeUpdate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            close(null,pst,con);
        }
        return a;
    }
}
