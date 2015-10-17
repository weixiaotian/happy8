package com.happy8.utils;

public class StringUtils {
	public static boolean isNullOrEmpty(String str){
		if(str == null || str.equals("")){
			return true;
		}
		return false;
	}
}
