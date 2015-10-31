package com.happy8;

import com.happy8.utils.HttpInvokeHelper;

public class TestTimeLine {
	static String urlTimelineInfo = "http://localhost:8080/happy8/timeLineinfo";
	static String urlTimelineInfoList1 = "http://localhost:8080/happy8/timelineinfolist?userid=13500000001&start=0&end=19";
	static String urlTimelineInfoList2 = "http://localhost:8080/happy8/timelineinfolist?userid=13500000000&start=20&end=39";
	static String urlreplaytimeline = "http://localhost:8080/happy8/replaytimeline";
	static String urldeletetimeline = "http://localhost:8080/happy8/deletetimeline?userid=13500000000&tlinfoid=64";
	
	public static void main(String[] args) {
//		for(int i=0;i<35;i++){
//			TimeLineInfoReqArgs timeReq = new TimeLineInfoReqArgs();
//			timeReq.setUserId("13500000000");
//			timeReq.setInfoContent("test " + i);
//			try {
//				String resp = HttpInvokeHelper.invokPost(urlTimelineInfo, JSON.toJSONString(timeReq));
//				System.out.println(resp);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		ReplayTimeLineReqArgs req = new ReplayTimeLineReqArgs();
//		req.setCommentedUserId("");
//		req.setPublishUserId("13500000001");
//		req.setTlInfoId(65);
//		req.setTxtContent("coment 1");
//		try {
//			String resp = HttpInvokeHelper.invokPost(urlreplaytimeline,JSON.toJSONString(req));
//			System.out.println(resp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			String resp = HttpInvokeHelper.invokGet(urldeletetimeline);
			System.out.println(resp);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			String resp = HttpInvokeHelper.invokGet(urlTimelineInfoList1);
			System.out.println(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String resp = HttpInvokeHelper.invokGet(urlTimelineInfoList2);
			System.out.println(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
