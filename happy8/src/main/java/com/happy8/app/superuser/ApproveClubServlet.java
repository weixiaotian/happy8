package com.happy8.app.superuser;

import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.app.coupon.AddCouponServlet;
import com.happy8.args.AddCouponReqArgs;
import com.happy8.args.AddCouponRspArgs;
import com.happy8.args.ApproveClubReqArgs;
import com.happy8.args.ClubItem;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

import push.PushHelper;

public class ApproveClubServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(ApproveClubServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			log.info("req body is" + body);
			ApproveClubReqArgs args = null;
			try{
				args = JSON.parseObject(body, ApproveClubReqArgs.class);
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
			
			if(args.getAction() != 0 && args.getAction() != 1 && args.getAction() != 2){
				log.error("action error " + args.getAction());
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			boolean isSuper = Happy8DAO.isUserSuperAdmin(args.getUserId());
			
			if(!isSuper){
				HttpTools.sendResponseOnlyStatusCode(response, 405);
				return;
			}
			log.info(String.format("clubid:%s action:%s", args.getUserId(),args.getAction()));
			Happy8DAO.updateClubStatus(args.getClubId(), args.getAction());
			ClubItem item = Happy8DAO.getClubItem(args.getClubId());
			if(item!=null){
				String pushToken = Happy8DAO.getUserPushToken(item.getOwnerId());
				if(!StringUtils.isNullOrEmpty(pushToken)){
					String title = args.getAction() == 1 ? "hi 亲，乐吧管理员已经审核通过了您的棋牌室！" : "hi 亲，乐吧管理员审核拒绝了您的棋牌室！";
					if(pushToken.length() > 44){
						PushHelper.sendIOSUnicast(pushToken, title);
					}else{
						PushHelper.sendAndroidUnicast(pushToken, title, title, title);
					}
				}
			}
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("ApproveClubServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
