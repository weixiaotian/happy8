package com.happy8.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
	public static boolean isNullOrEmpty(String str){
		if(str == null || str.equals("")){
			return true;
		}
		return false;
	}
	
	public static Date parse2Date(String str) throws ParseException{
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.parse(str);
	}
	
	public static String Date2String(Date date){
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
}
