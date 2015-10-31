package com.happy8.app.timeline;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.TimeLineInfoReqArgs;
import com.happy8.args.TimeLineInfoRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class TimeLineInfoServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(TimeLineInfoServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			TimeLineInfoReqArgs args = null;
			try{
				args = JSON.parseObject(body, TimeLineInfoReqArgs.class);
			}catch(Exception ex){
				log.error("parse error req: "+body, ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(args.getUserId())){
				log.error("req userid is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			
			long id = Happy8DAO.insertTimeLineInfo(args.getUserId(), args.getInfoContent());
			
			List<String> publishUsers = Happy8DAO.getFriendUserIdList(args.getUserId());
			publishUsers.add(args.getUserId());//加入自己
			for(String userId:publishUsers){
				Happy8DAO.insertUserTimeLine(userId, id);
			}
			TimeLineInfoRspArgs res = new TimeLineInfoRspArgs();
			res.setTlInfoId(id);
			HttpTools.sendOkResponse(response, JSON.toJSONString(res));
			
		}catch(Exception ex){
			log.error("TimeLineInfoServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}

