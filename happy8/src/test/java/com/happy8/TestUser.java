package com.happy8;

import com.alibaba.fastjson.JSON;
import com.happy8.args.UserInfoArgs;
import com.happy8.args.UserPasswordArgs;
import com.happy8.utils.HttpInvokeHelper;

public class TestUser {
	public static void main(String[] args) {
		String urlget = "http://119.254.97.20:8080/happy8/getuserinfo?userid=13500000000";
		String urlreg = "http://119.254.97.20:8080/happy8/registeruser";
		String urllupdate = "http://119.254.97.20:8080/happy8/setuserinfo";
		String urllogin = "http://119.254.97.20:8080/happy8/userlogin";
		String urlresetpassword = "http://119.254.97.20:8080/happy8/resetpassword";
		String urllogout = "http://119.254.97.20:8080/happy8/logout?userid=13500000000";
		try {
			String resp = "";
			try{
				UserPasswordArgs regArgs = new UserPasswordArgs();
				regArgs.setUserId("13500000000");
				regArgs.setPassword("test123");
				 resp = HttpInvokeHelper.invokPost(urlreg, JSON.toJSONString(regArgs));
				System.out.println(resp);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			try{
				UserPasswordArgs pwdErrArgs = new UserPasswordArgs();
				pwdErrArgs.setUserId("15131196979");
				pwdErrArgs.setPassword("test123");
				resp = HttpInvokeHelper.invokPost(urllogin, JSON.toJSONString(pwdErrArgs));
			}catch(Exception ex){
				ex.printStackTrace();
			}
			UserPasswordArgs pwdReset = new UserPasswordArgs();
			pwdReset.setUserId("13500000000");
			pwdReset.setPassword("test12");
			resp = HttpInvokeHelper.invokPost(urlresetpassword, JSON.toJSONString(pwdReset));
			
			UserInfoArgs userInfo = new UserInfoArgs();
			userInfo.setAvatarUrl("http://test.com");
			userInfo.setBrief("足球运动员");
			userInfo.setUserId("13500000000");
			userInfo.setGender(2);
			userInfo.setSignature("卡卡");
			resp = HttpInvokeHelper.invokPost(urllupdate, JSON.toJSONString(userInfo));
			
			resp = HttpInvokeHelper.invokGet(urlget);
			
			resp = HttpInvokeHelper.invokGet(urllogout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
