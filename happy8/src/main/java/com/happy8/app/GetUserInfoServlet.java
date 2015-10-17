package com.happy8.app;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.UserInfoArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class GetUserInfoServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(GetUserInfoServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			String userId = request.getParameter("userid");
			if(StringUtils.isNullOrEmpty(userId)){
				log.error("user args id is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			UserInfoArgs args = Happy8DAO.getUserInfo(userId);
			if(args == null){
				log.error("user id is not find " + userId);
				HttpTools.sendResponseOnlyStatusCode(response, 404);
				return;
			}
			HttpTools.sendOkResponse(response, JSON.toJSONString(args));
		}catch(Exception ex){
			log.error("GetUserInfoServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
