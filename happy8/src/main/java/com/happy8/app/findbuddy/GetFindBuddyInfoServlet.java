package com.happy8.app.findbuddy;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.FindBuddyInfoItem;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;

public class GetFindBuddyInfoServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(GetFindBuddyInfoServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			int fdinfoid = 0;
			try{
				fdinfoid = Integer.valueOf(request.getParameter("fdinfoid"));
			}catch(Exception ex){
				log.error("parse index error",ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			FindBuddyInfoItem res = Happy8DAO.getFindBuddyInfo(fdinfoid);
			HttpTools.sendOkResponse(response, JSON.toJSONString(res));
		}catch(Exception ex){
			log.error("GetFindBuddyInfoServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
