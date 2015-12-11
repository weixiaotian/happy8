package com.happy8.app.bookclub;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happy8.app.findbuddy.CheckOrderItem;
import com.happy8.args.OrderDetailItem;
import com.happy8.args.OrderTableReqArgs;
import com.happy8.args.OrderTableReqArgs2;
import com.happy8.args.OrderTableRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;
import com.happy8.weixinpay.PayInfo;
import com.happy8.weixinpay.PayUtils;

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
			if(StringUtils.isJsonFieldString(body, "detail")){
				try{
					OrderTableReqArgs2 args2 = JSON.parseObject(body, OrderTableReqArgs2.class);
					args = new OrderTableReqArgs();
					args.setAmount(args2.getAmount());
					args.setDetail(JSON.parseArray(args2.getDetail(), OrderDetailItem.class));
				}catch(Exception ex){
					log.error("parse error req: "+body, ex);
					HttpTools.sendResponseOnlyStatusCode(response, 400);
					return;
				}
			}else{
				try{
					args = JSON.parseObject(body, OrderTableReqArgs.class);
				}catch(Exception ex){
					log.error("parse error req: "+body, ex);
					HttpTools.sendResponseOnlyStatusCode(response, 400);
					return;
				}
			}
			
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(args.getDetail() == null || args.getDetail().size() == 0){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			Date now = new Date();
			for(OrderDetailItem item : args.getDetail()){
				if(StringUtils.isNullOrEmpty(item.getUserId())){
					log.error("req userid is null");
					HttpTools.sendResponseOnlyStatusCode(response, 400);
					return;
				}
				
				CheckOrderItem check = Happy8DAO.getCheckOrder(item.getTableId(), StringUtils.parse2Date(item.getDate()), item.getGameTime());
				
				if(check!=null && check.getStatus() == 1){
					log.error("table is ordered and also is pay");
					HttpTools.sendResponseOnlyStatusCode(response, 406);
					return;
				}
				
				
				if(check!=null){
					if(now.getTime() - check.getCreateDate().getTime() < 15 * 60 * 1000){
						log.error(String.format("table is order no pay createdate%%s now:%", check.getCreateDate(),now));
						HttpTools.sendResponseOnlyStatusCode(response, 406);
						return;
					}
				}
				
				if(check!=null){
					Happy8DAO.deleteNoPayOrder(check.getOrderId());
				}
			}
			
			long res = Happy8DAO.insertOrder(args.getAmount());
			for(OrderDetailItem item : args.getDetail()){
				Happy8DAO.insertOrderDetail(res, item.getTableId(), StringUtils.parse2Date(item.getDate()),item.getGameTime(), item.getUserId());
			}
			OrderTableRspArgs resArgs = new OrderTableRspArgs();
			resArgs.setCurrentTime(StringUtils.Date2String(now));
			resArgs.setOrderId(res);
			PayInfo payInfo = PayUtils.createPayInfo(args, res);
			Map<String, Object> payRes = PayUtils.postOrder(payInfo);
			String code = String.valueOf(payRes.get("return_code"));
			String trade_type = String.valueOf(payRes.get("trade_type"));
			String prepay_id = String.valueOf(payRes.get("prepay_id"));
			String nonce_str = String.valueOf(payRes.get("nonce_str"));
			if(!code.equals("SUCCESS")){
				log.error("weixin pay error");
				HttpTools.sendResponseOnlyStatusCode(response, 406);
				return;
			}
			resArgs.setPrepay_id(prepay_id);
			resArgs.setTrade_type(trade_type);
			resArgs.setNonce_str(nonce_str);
			HttpTools.sendOkResponse(response, JSON.toJSONString(resArgs,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
		}catch(Exception ex){
			log.error("OrderTableServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}