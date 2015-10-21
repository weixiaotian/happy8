package com.happy8.app.friend;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.app.findbuddy.ReplayFindBuddyInfoServlet;
import com.happy8.args.AddFriendReqArgs;
import com.happy8.args.ReplayFindBuddyReqArgs;
import com.happy8.args.ReplayFindBuddyRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class AddFriendServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(AddFriendServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			AddFriendReqArgs args = null;
			try{
				args = JSON.parseObject(body, AddFriendReqArgs.class);
			}catch(Exception ex){
				log.error("parse error req: "+body, ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(args.getUserId()) || StringUtils.isNullOrEmpty(args.getFriendUserId())){
				log.error("req userid is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			Happy8DAO.insertAddFriendReq(args.getFriendUserId(), args.getUserId());// 给被请求方插入一条请求记录
			//TODO 给被请求方发push通知
			
			HttpTools.sendResponseOnlyStatusCode(response, 200);
			
		}catch(Exception ex){
			log.error("AddFriendServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
