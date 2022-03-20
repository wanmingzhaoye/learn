package com.djt.util;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bigdata
 * @create 2021-08-24-15:32
 */
public class JavaDBFReader {

    public static void main(String args[]) throws IOException {
        //String path = "D:\\data\\db";
        //getFile(path);
        String file = "D:\\data\\db\\FOXUSER.DBF";
        String file2 = "D:\\data\\db\\mydb.dbf";
        readDbf(file,"GBK");

    }

    public static void dbfr(String url,String table) {
        System.out.println(" CREATE TABLE "+table.substring(0,table.length()-4)+" ( ");
        try {
            InputStream inputStream = new FileInputStream(new File(url));
            DBFReader reader = new DBFReader(inputStream);
            int numberOfFields = reader.getFieldCount();
            String sz = "";
            for (int i = 0; i < numberOfFields; i++) {
                DBFField field = reader.getField(i);
                if(i==0) {
                    System.out.println("   "+field.getName()+" varchar("+field.getLength() +")   ");
                }else {
                    System.out.println("   "+","+field.getName()+" varchar("+field.getLength() +")   ");
                }
//				System.out.println(field.getName() + "___" + field.getLength() + "___" + field.getType());
            }
            System.out.println();
            Object[] rowObjects;
//			while ((rowObjects = reader.nextRecord()) != null) {
//				for (int i = 0; i < rowObjects.length; i++) {
//					System.out.println(rowObjects[i]);
//				}
//			}
            inputStream.close();
            System.out.println(" ); ");
        } catch (DBFException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getFile(String path) {
        // get file list where the path has
        File file = new File(path);
        // get the folder list
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
//				System.out.println(array[i].getPath() + array[i].getName());
                dbfr(array[i].getPath(),array[i].getName());
//                System.out.println(array[i].getPath());
            } else if (array[i].isDirectory()) {
                getFile(array[i].getPath());
            }
        }
    }

    public static List<Map<String, String>> readDbf(String path, String charsetName) throws IOException {
        List<Map<String, String>> rowList = new ArrayList<>();
//		InputStream fis = new FileInputStream(path);
        DBFReader dbfReader = new DBFReader(new FileInputStream(path), Charset.forName(charsetName));

        System.out.println("recordnum="+dbfReader.getRecordCount());

        Object[] ob = dbfReader.nextRecord();
        for (int i=0;i<ob.length;i++){
            System.out.println(ob[i]);
        }

        Object[] rowValues;
        while ((rowValues = dbfReader.nextRecord()) != null) {
            Map<String, String> rowMap = new HashMap<String, String>();
            for (int i = 0; i < rowValues.length; i++) {
                rowMap.put(dbfReader.getField(i).getName(), String.valueOf(rowValues[i]).trim());
                System.out.println(dbfReader.getField(i).getName()+"==="+String.valueOf(rowValues[i]).trim());
            }
//			System.out.println(rowMap);
            rowList.add(rowMap);
        }
        dbfReader.close();
//		fis.close();
        return rowList;
    }
}
