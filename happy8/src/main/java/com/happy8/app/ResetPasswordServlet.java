package com.happy8.app;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.UserPasswordArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;

public class ResetPasswordServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(ResetPasswordServlet.class);
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
			int statusCode = Happy8DAO.resetPassword(args.getUserId(), args.getPassword());
			HttpTools.sendResponseOnlyStatusCode(response, statusCode);
		}catch(Exception ex){
			log.error("ResetPasswordServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
