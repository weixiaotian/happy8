package com.happy8.app.club;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.AddFavoriteReqArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class ProcessFavoriteServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(ProcessClubServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String action = request.getParameter("action");
			if(StringUtils.isNullOrEmpty(action)){
				log.error("action param is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			if(action.equals("add")){
				processAdd(request, response);
			}else if(action.equals("delete")){
				processDelete(request, response);
			}else{
				log.error("action unkonw : " + action);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
			}			
		}catch(Exception ex){
			log.error("ProcessClubServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}

	private void processAdd(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String body = HttpTools.getRequestBody(request);
		AddFavoriteReqArgs args = null;
		try{
			args = JSON.parseObject(body, AddFavoriteReqArgs.class);
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
		
		Happy8DAO.insertFavoriteClub(args.getUserId(), args.getClubId());
		
		HttpTools.sendResponseOnlyStatusCode(response, 200);
	}
	
	private void processDelete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String body = HttpTools.getRequestBody(request);
		AddFavoriteReqArgs args = null;
		try{
			args = JSON.parseObject(body, AddFavoriteReqArgs.class);
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
		
		Happy8DAO.delFavoriteClub(args.getUserId(), args.getClubId());
		
		HttpTools.sendResponseOnlyStatusCode(response, 200);
	}
}
