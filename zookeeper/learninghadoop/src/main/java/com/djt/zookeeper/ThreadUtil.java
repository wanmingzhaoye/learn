package com.djt.zookeeper;
/**
 * 线程工具类
 * @author dajiangtai
 *
 */
public class ThreadUtil {

	@SuppressWarnings("static-access")//去掉警告
	public static void sleep(long millions){
		try {
			Thread.currentThread().sleep(millions);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		while(true){
			System.out.println((long)(Math.random() *5000));
		}

	}
}
