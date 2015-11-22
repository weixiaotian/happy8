package com.happy8.app.club;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happy8.args.ClubItem;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class QueryClubListServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(QueryClubListServlet.class);
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
			String index = request.getParameter("index");
			String type = request.getParameter("type");
			
			if(StringUtils.isNullOrEmpty(index)){
				log.error("index is null ");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(!type.equals("tel") && !type.equals("addr")){
				log.error("error type :" + type);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			List<ClubItem> res = Happy8DAO.queryClubList(index, type, start, end);
			
			HttpTools.sendOkResponse(response, JSON.toJSONString(res,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
		}catch(Exception ex){
			log.error("QueryClubListServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
