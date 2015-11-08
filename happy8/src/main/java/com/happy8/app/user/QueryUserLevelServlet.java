package com.happy8.app.user;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Authentication.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.UserLevelArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class QueryUserLevelServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(QueryUserLevelServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			String userId = request.getParameter("userid");
			if(StringUtils.isNullOrEmpty(userId)){
				log.error("user args id is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			UserLevelArgs args = Happy8DAO.getUserLevel(userId);
			if(args == null){
				log.error("user id is not find " + userId);
				HttpTools.sendResponseOnlyStatusCode(response, 404);
				return;
			}
			HttpTools.sendOkResponse(response, JSON.toJSONString(args));
		}catch(Exception ex){
			log.error("QueryUserLevelServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
