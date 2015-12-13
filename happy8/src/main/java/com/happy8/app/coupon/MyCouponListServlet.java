package com.happy8.app.coupon;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happy8.app.bookclub.BookClubListServlet;
import com.happy8.args.BookClubItem;
import com.happy8.args.CouponItem;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class MyCouponListServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(MyCouponListServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			int start = 0;
			int end = 0;
			int index = 0;
			try{
				start = Integer.valueOf(request.getParameter("start"));
				end = Integer.valueOf(request.getParameter("end"));
				index = Integer.valueOf(request.getParameter("index"));
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
			List<CouponItem> res = null;
			if(index == 0){
				res = Happy8DAO.getAllMyCouponList(start, end);
			}else if(index == 1){
				res = Happy8DAO.getUnConsumeCouponList(start, end);
			}else if(index == 2){
				res = Happy8DAO.getConsumeCouponList(userId, start, end);
			}else{
				log.error("parse index error" + index);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			HttpTools.sendOkResponse(response, JSON.toJSONString(res,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
		}catch(Exception ex){
			log.error("BookClubListServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
