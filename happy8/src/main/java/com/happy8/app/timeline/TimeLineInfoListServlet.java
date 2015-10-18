package com.happy8.app.timeline;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.FindBuddyInfoItem;
import com.happy8.args.TimeLineInfoItem;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class TimeLineInfoListServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(TimeLineInfoListServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			int start = 0;
			int end = 0;
			try{
				start = Integer.valueOf(request.getParameter("start"));
				end = Integer.valueOf(request.getParameter("end"));
			}catch(Exception ex){
				log.error("parse index error",ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			String userId = request.getParameter("userid");
			if(StringUtils.isNullOrEmpty(userId)){
				log.error("req userid is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			String selfSend = request.getParameter("selfsend");
			List<TimeLineInfoItem> res = null;
			if(selfSend!=null && selfSend.equals("1")){//查询自己的
				 res = Happy8DAO.getTimeLineSelfSendList(userId, start, end);
			}else{
				res = Happy8DAO.getTimeLineList(userId, start, end);
			}
			
			HttpTools.sendOkResponse(response, JSON.toJSONString(res));
		}catch(Exception ex){
			log.error("TimeLineInfoListServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
