package com.happy8;

import com.alibaba.fastjson.JSON;
import com.happy8.app.findbuddy.ReplayFindBuddyInfoServlet;
import com.happy8.args.FindBuddyInfoReqArgs;
import com.happy8.args.ReplayFindBuddyReqArgs;
import com.happy8.utils.HttpInvokeHelper;

public class TestFindBuddy {
	static String urlfindBuddyInfo = "http://localhost:8080/happy8/findbuddyinfo";
	static String urlfindbuddyinfolist1 = "http://localhost:8080/happy8/findbuddyinfolist?start=0&end=19";
	static String urlfindbuddyinfolist2 = "http://localhost:8080/happy8/findbuddyinfolist?start=20&end=39";
	static String urlreplayfindbuddy = "http://localhost:8080/happy8/replayfindbuddy";
	
	public static void main(String[] args) {
		try{
//			for(int i=0;i<35;i++){
//				FindBuddyInfoReqArgs req = new FindBuddyInfoReqArgs();
//				req.setUserId("13500000000");
//				req.setInfoContent("找牌友 " + i);
//				String rep = HttpInvokeHelper.invokPost(urlfindBuddyInfo, JSON.toJSONString(req));
//				System.out.println(rep);
//			}
			
			ReplayFindBuddyReqArgs req = new ReplayFindBuddyReqArgs();
			req.setBdInfoId(1);
			//req.setCommentedUserId("13500000001");
			req.setPublishUserId("13500000001");
			req.setTxtContent("comment 1");
			//String rep = HttpInvokeHelper.invokPost(urlreplayfindbuddy, JSON.toJSONString(req));
			//.out.println(rep);
			
			String list = HttpInvokeHelper.invokGet(urlfindbuddyinfolist1);
			System.out.println(list);
			
			String list2 = HttpInvokeHelper.invokGet(urlfindbuddyinfolist2);
			System.out.println(list2);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
