package com.happy8.app.timeline;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.ReplayTimeLineReqArgs;
import com.happy8.args.ReplayTimeLineRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class ReplayTimeLineServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(ReplayTimeLineServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			ReplayTimeLineReqArgs args = null;
			try{
				args = JSON.parseObject(body, ReplayTimeLineReqArgs.class);
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
			
			long id = Happy8DAO.insertTimeLineComment(args.getTlInfoId(),args.getPublishUserId(), args.getCommentedUserId(), args.getTxtContent());
			ReplayTimeLineRspArgs res = new ReplayTimeLineRspArgs();
			res.setCommentId(id);;
			HttpTools.sendOkResponse(response, JSON.toJSONString(res));
			
		}catch(Exception ex){
			log.error("ReplayTimeLineServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
