package com.happy8.app.user;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.StatusCode;
import com.happy8.args.UserInfoArgs;
import com.happy8.args.UserPasswordArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class UserLoginServlet extends HttpServlet {
	
	private static Logger log = LoggerFactory.getLogger(UserLoginServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			UserPasswordArgs args = null;
			try{
				args = JSON.parseObject(body, UserPasswordArgs.class);
			}catch(Exception ex){
				log.error("parse error req: "+body, ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			int statusCode = Happy8DAO.userLogin(args.getUserId(), args.getPassword());
			if(statusCode == 200){
				Happy8DAO.updateUserStatus(args.getUserId(), 1);
			}
			StatusCode code = new StatusCode();
			code.setStatusCode(statusCode);
			HttpTools.sendOkResponse(response, JSON.toJSONString(code));
		}catch(Exception ex){
			log.error("UserLoginServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
