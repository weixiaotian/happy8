package com.happy8.app.findbuddy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.FindBuddyInfoReqArgs;
import com.happy8.args.FindBuddyInfoRspArgs;
import com.happy8.args.ReplayFindBuddyReqArgs;
import com.happy8.args.ReplayFindBuddyRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class ReplayFindBuddyInfoServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(ReplayFindBuddyInfoServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			ReplayFindBuddyReqArgs args = null;
			try{
				args = JSON.parseObject(body, ReplayFindBuddyReqArgs.class);
			}catch(Exception ex){
				log.error("parse error req: "+body, ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(args.getPublishUserId())){
				log.error("req userid is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			long id = Happy8DAO.insertFindBuddyComment(args.getBdInfoId(),args.getPublishUserId(), args.getCommentedUserId(), args.getTxtContent());
			ReplayFindBuddyRspArgs res = new ReplayFindBuddyRspArgs();
			res.setCommentId(id);;
			HttpTools.sendOkResponse(response, JSON.toJSONString(res));
			
		}catch(Exception ex){
			log.error("ReplayFindBuddyInfoServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
