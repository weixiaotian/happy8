package com.happy8;

import com.happy8.utils.HttpInvokeHelper;

public class TestHttp {
	public static void main(String[] args) {
		String url = "http://localhost:8080/happy8/getuserinfo?userid=13500000000";
		try {
			String resp = HttpInvokeHelper.invokGet(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
