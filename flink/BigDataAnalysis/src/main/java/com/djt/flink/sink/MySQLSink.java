package com.djt.flink.sink;
import com.djt.flink.config.GlobalConfig;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MySQLSink extends RichSinkFunction<Tuple4<String,String, String, Integer>> {
    private Connection connection;
    private PreparedStatement preparedStatement;
    @Override
    public void open(Configuration parameters) throws Exception {
        System.out.println("open-------------------");
        super.open(parameters);
        // 加载JDBC驱动
        Class.forName(GlobalConfig.DRIVER_CLASS);
        // 获取数据库连接
        connection = DriverManager.getConnection(GlobalConfig.DB_URL, GlobalConfig.USER_MAME, GlobalConfig.PASSWORD);
        //prepareStatement 预加载 ？号占位符
        preparedStatement = connection.prepareStatement(GlobalConfig.INSERTSQL);
        super.open(parameters);
    }
 
    @Override
    public void close() throws Exception {
        super.close();
        if(preparedStatement != null){
            preparedStatement.close();
        }
        if(connection != null){
            connection.close();
        }
        super.close();
    }
 
    @Override
    public void invoke(Tuple4<String,String, String, Integer> value, Context context) throws Exception {
        try {
            System.out.println("invoke-------------------");
            String time = value.f0;
            String provinceCode = value.f1;
            String cityCode = value.f2;
            int sum = value.f3;
            preparedStatement.setString(1,time);
            preparedStatement.setString(2,provinceCode);
            preparedStatement.setString(3,cityCode);
            preparedStatement.setInt(4,sum);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
