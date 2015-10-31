package com.happy8;

import com.alibaba.fastjson.JSON;
import com.happy8.args.AddClubReqArgs;
import com.happy8.args.AddFavoriteReqArgs;
import com.happy8.args.ClubItem;
import com.happy8.args.DeleteClubReqArgs;
import com.happy8.utils.HttpInvokeHelper;

public class TestClub {
	
	static String urlAddClub = "http://localhost:8080/happy8/processclub?action=add";
	static String urlUpdateClub = "http://localhost:8080/happy8/processclub?action=update";
	static String urlDeleteClub = "http://localhost:8080/happy8/processclub?action=delete";
	static String urlNeerby = "http://localhost:8080/happy8/nearbyclublist?longitude=116.38955&latitude=39.928167&index=1&start=0&end=19";
	static String urlQueryClub = "http://localhost:8080/happy8/queryclublist?index=135&type=tel&start=0&end=19";
	static String urlAddFavorite = "http://localhost:8080/happy8/processfavorite?action=add";
	static String urlDeleteFavorite = "http://localhost:8080/happy8/processfavorite?action=delete";
	static String urlGetFavorite = "http://localhost:8080/happy8/favoriteclublist?userid=13500000000&start=0&end=19";
	
	public static void main(String[] args) {
//		AddClubReqArgs req = new AddClubReqArgs();
//		req.setAddr("add1");
//		req.setClubImageUrl("http");
//		req.setLatitude(39.928167);
//		req.setLongitude(116.389550);
//		req.setOwnerId("13500000000");
//		req.setPalyStyle("ddd");
//		req.setPhone("+8613500000000");
//		req.setSale(23.33);
//		try {
//			String resp = HttpInvokeHelper.invokPost(urlAddClub, JSON.toJSONString(req));
//			System.out.println(resp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		ClubItem req = new ClubItem();
//		req.setPhone("+8613503400000");
//		req.setClubId(1);
//		try {
//			String resp = HttpInvokeHelper.invokPost(urlUpdateClub, JSON.toJSONString(req));
//			System.out.println(resp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		DeleteClubReqArgs req = new DeleteClubReqArgs();
//		req.setClubId(1);
//		req.setOwnerId("13500000000");
//		try {
//			String resp = HttpInvokeHelper.invokPost(urlDeleteClub, JSON.toJSONString(req));
//			System.out.println(resp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			String resp = HttpInvokeHelper.invokGet(urlNeerby);
//			System.out.println(resp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			String resp = HttpInvokeHelper.invokGet(urlQueryClub);
//			System.out.println(resp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		AddFavoriteReqArgs req = new AddFavoriteReqArgs();
//		req.setUserId("13500000000");
//		req.setClubId(2);
//		try {
//			String resp = HttpInvokeHelper.invokPost(urlAddFavorite, JSON.toJSONString(req));
//			System.out.println(resp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			String resp = HttpInvokeHelper.invokGet(urlGetFavorite);
			System.out.println(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
