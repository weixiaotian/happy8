package com.happy8.app.bookclub;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happy8.args.OrderItem;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class BookClubListServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(BookClubListServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			int start = 0;
			int end = 0;
			try{
				start = Integer.valueOf(request.getParameter("start"));
				end = Integer.valueOf(request.getParameter("end"));
			}catch(Exception ex){
				log.error("parse index error",ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			String userId = request.getParameter("userid");
			if(StringUtils.isNullOrEmpty(userId)){
				log.error("req userid is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			Date now = new Date();
			long time = now.getTime() - 15 * 60 * 1000;
			Date use = new Date(time);
			List<OrderItem> res = Happy8DAO.getOrderList(userId , use, start, end);
			HttpTools.sendOkResponse(response, JSON.toJSONString(res,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
		}catch(Exception ex){
			log.error("BookClubListServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
