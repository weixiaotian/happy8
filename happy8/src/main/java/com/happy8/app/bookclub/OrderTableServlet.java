package com.happy8.app.bookclub;

import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happy8.app.findbuddy.CheckOrderItem;
import com.happy8.args.OrderTableReqArgs;
import com.happy8.args.OrderTableRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class OrderTableServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(OrderTableServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			OrderTableReqArgs args = null;
			try{
				args = JSON.parseObject(body, OrderTableReqArgs.class);
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
			
			CheckOrderItem check = Happy8DAO.getCheckOrder(args.getTableId(), StringUtils.parse2Date(args.getDate()), args.getGameTime());
			
			if(check!=null && check.getStatus() == 1){
				log.error("table is ordered and also is pay");
				HttpTools.sendResponseOnlyStatusCode(response, 406);
				return;
			}
			
			Date now = new Date();
			if(check!=null){
				if(now.getTime() - check.getCreateDate().getTime() < 15 * 60 * 1000){
					log.error(String.format("table is order no pay createdate%%s now:%", check.getCreateDate(),now));
					HttpTools.sendResponseOnlyStatusCode(response, 406);
					return;
				}
			}
			
			if(check!=null){
				Happy8DAO.deleteNoPayOrder(args.getTableId(), StringUtils.parse2Date(args.getDate()), args.getGameTime());
			}
			
			long res = Happy8DAO.insertOrder(args.getTableId(), StringUtils.parse2Date(args.getDate()), args.getGameTime(), args.getUserId());
			
			OrderTableRspArgs resArgs = new OrderTableRspArgs();
			resArgs.setCurrentTime(StringUtils.Date2String(now));
			resArgs.setOrderId(res);
			HttpTools.sendOkResponse(response, JSON.toJSONString(resArgs,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
		}catch(Exception ex){
			log.error("OrderTableServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
