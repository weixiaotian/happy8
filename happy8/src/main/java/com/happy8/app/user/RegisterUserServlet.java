package com.happy8.app.user;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.UserInfoArgs;
import com.happy8.args.UserPasswordArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;

public class RegisterUserServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(RegisterUserServlet.class);
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
			UserInfoArgs userInfo = Happy8DAO.getUserInfo(args.getUserId());
			if(userInfo!=null){
				HttpTools.sendResponseOnlyStatusCode(response, 405);
				return;
			}
			Happy8DAO.registerUser(args.getUserId(), args.getPassword());
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("RegisterUserServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
