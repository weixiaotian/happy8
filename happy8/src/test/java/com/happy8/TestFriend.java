package com.happy8;

import com.alibaba.fastjson.JSON;
import com.happy8.args.AddFriendReqArgs;
import com.happy8.utils.HttpInvokeHelper;

public class TestFriend {
	static String urlAddFriend = "http://localhost:8080/happy8/addfriend";
	static String urlAllowaddfriend = "http://localhost:8080/happy8/allowaddfriend";
	static String urldeletefriend = "http://localhost:8080/happy8/deletefriend";
	public static void main(String[] args) {
		AddFriendReqArgs req = new AddFriendReqArgs();
		req.setUserId("13500000000");
		req.setFriendUserId("13500000001");
		
		try {
			String resp = HttpInvokeHelper.invokPost(urlAddFriend, JSON.toJSONString(req));
			System.out.println(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		AddFriendReqArgs allReq = new AddFriendReqArgs();
		allReq.setUserId("13500000001");
		allReq.setFriendUserId("13500000000");
		
		try {
			String resp = HttpInvokeHelper.invokPost(urlAllowaddfriend, JSON.toJSONString(allReq));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
