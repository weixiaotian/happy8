package com.happy8.app.superuser;

import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.PublishSystemNotifyReqArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class PublishSystemNotifyServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(PublishSystemNotifyServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			log.info("req body is" + body);
			PublishSystemNotifyReqArgs args = null;
			try{
				args = JSON.parseObject(body, PublishSystemNotifyReqArgs.class);
			}catch(Exception ex){
				log.error("parse error req: "+body, ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
						
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
					
			String userId = request.getParameter("userid");
			boolean isSuper = Happy8DAO.isUserSuperAdmin(userId);
			
			if(!isSuper){
				HttpTools.sendResponseOnlyStatusCode(response, 405);
				return;
			}
			Date sendTime = StringUtils.parse2Date(args.getSendTime());
			Happy8DAO.insertSystemNotify(args.getTitle(),args.getContent(),sendTime);
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("PublishSystemNotifyServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
