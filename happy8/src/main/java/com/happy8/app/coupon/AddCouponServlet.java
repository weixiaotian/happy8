package com.happy8.app.coupon;

import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happy8.args.AddCouponReqArgs;
import com.happy8.args.AddCouponRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class AddCouponServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(AddCouponServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			AddCouponReqArgs args = null;
			try{
				args = JSON.parseObject(body, AddCouponReqArgs.class);
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
			
			boolean isSuper = Happy8DAO.isUserSuperAdmin(args.getUserId());
			
			if(!isSuper){
				HttpTools.sendResponseOnlyStatusCode(response, 405);
				return;
			}
			
			UUID guid = UUID.randomUUID();
			Happy8DAO.insertCoupon(guid.toString(), args.getType(), args.getDisCount(), args.getValue(), args.getStartAmout(),args.getExpireTime());
			AddCouponRspArgs resArgs = new AddCouponRspArgs();
			resArgs.setCouponId(guid.toString());
			HttpTools.sendOkResponse(response, JSON.toJSONString(resArgs,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
		}catch(Exception ex){
			log.error("AddCouponServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
