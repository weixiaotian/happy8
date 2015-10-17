package com.happy8.app;

import java.util.ArrayList;
import java.util.List;

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

public class UpdateUserInfoServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(UpdateUserInfoServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			UserInfoArgs args = null;
			try{
				args = JSON.parseObject(body, UserInfoArgs.class);
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
			String sqlFormat = "update ha_user set %s where userid = ?";
			
			StringBuilder sb = new StringBuilder();
			
			List<Object> values = new ArrayList<Object>();
			if(!StringUtils.isNullOrEmpty(args.getAvatarUrl())){
				sb.append(", avatarurl = ? ");
				values.add(args.getAvatarUrl());
			}
			if(!StringUtils.isNullOrEmpty(args.getBrief())){
				sb.append(", brief = ? ");
				values.add(args.getBrief());
			}
			if(!StringUtils.isNullOrEmpty(args.getSignature())){
				sb.append(", signature = ? ");
				values.add(args.getSignature());
			}
			if(args.getGender() != 0){
				sb.append(", gender = ? ");
				values.add(args.getGender());
			}
			if(StringUtils.isNullOrEmpty(sb.toString())){
				log.error("req no filed: "+body);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			String strField = sb.toString().indexOf(",") == 0 ? sb.toString().substring(1) : sb.toString();
			values.add(args.getUserId());
			int statusCode = Happy8DAO.updateUserInfo(String.format(sqlFormat, strField), values.toArray());
			HttpTools.sendResponseOnlyStatusCode(response, statusCode);
		}catch(Exception ex){
			log.error("UpdateUserInfoServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
