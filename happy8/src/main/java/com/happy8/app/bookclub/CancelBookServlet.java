package com.happy8.app.bookclub;

import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.happy8.args.OrderStatusAmount;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.weixinpay.PayUtils;

public class CancelBookServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(CancelBookServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			long orderid = Long.parseLong(request.getParameter("orderid"));
			OrderStatusAmount amount = Happy8DAO.getOrderAmountAndStatus(orderid);
			if(amount == null){
				HttpTools.sendResponseOnlyStatusCode(response, 404);
				return;
			}
			if(amount.getPayStatus() == 0){//未支付
				Happy8DAO.deleteOrderById(orderid);
				HttpTools.sendResponseOnlyStatusCode(response, 200);
				return;
			}else if(amount.getPayStatus() == 1){//已支付
				int totalFee = (int)(100 * amount.getAmount());
				Map<String,Object> res = PayUtils.postRefundOrder(orderid, totalFee);
				
				String code = String.valueOf(res.get("return_code"));
				if(!code.equals("SUCCESS")){
					log.error("weixin pay error");
					HttpTools.sendResponseOnlyStatusCode(response, 406);
					return;
				}
				Happy8DAO.updateOrderPayStatus(orderid, 3);//change status
				HttpTools.sendResponseOnlyStatusCode(response, 200);
				return;
			}else{
				log.error("status error order id is" + orderid);
				HttpTools.sendResponseOnlyStatusCode(response, 404);
				return;
			}
			
		}catch(Exception ex){
			log.error("CancelBookServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
