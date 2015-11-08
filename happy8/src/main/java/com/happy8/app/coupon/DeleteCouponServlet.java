package com.happy8.app.coupon;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.happy8.app.bookclub.CancelBookServlet;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;

public class DeleteCouponServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(DeleteCouponServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			String userId = request.getParameter("userid");
			String couponid = request.getParameter("couponid");
			
			if(!Happy8DAO.isUserSuperAdmin(userId)){
				HttpTools.sendResponseOnlyStatusCode(response, 405);
				return;
			}
			
			Happy8DAO.deleteCoupon(couponid);
			
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("DeleteCouponServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
