package com.happy8.app.bookclub;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.OrderStatusAmount;
import com.happy8.args.UpdateOrderStatusItem;
import com.happy8.args.UpdateOrderStatusReqArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;
import com.happy8.utils.TokenUtils;

public class UpdateOrderStatusServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(UpdateOrderStatusServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			UpdateOrderStatusReqArgs args = null;
			
			try{
				args = JSON.parseObject(body, UpdateOrderStatusReqArgs.class);
			}catch(Exception ex){
				log.error("parse error req: "+body, ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			String token = args.getToken();
			if(StringUtils.isNullOrEmpty(token)){
				log.error("token is null.");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			UpdateOrderStatusItem reqItem = TokenUtils.parseToken(token);
			if(reqItem == null){
				HttpTools.sendResponseOnlyStatusCode(response, 401);
				return;
			}
			
			OrderStatusAmount orderStatus = Happy8DAO.getOrderAmountAndStatus(reqItem.getOrderId());
			
			if(reqItem.getStatus() - orderStatus.getPayStatus() != 1){
				log.error(String.format("req update status error,req status is %s now status is %s", reqItem.getStatus(),orderStatus.getPayStatus()));
				HttpTools.sendResponseOnlyStatusCode(response, 405);
				return;
			}
			Happy8DAO.updateOrderPayStatus(reqItem.getOrderId(), reqItem.getStatus());
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("UpdateOrderStatusServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
