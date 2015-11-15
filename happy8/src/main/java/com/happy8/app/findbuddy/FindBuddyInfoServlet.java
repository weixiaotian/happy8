package com.happy8.app.findbuddy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.FindBuddyInfoItem;
import com.happy8.args.FindBuddyInfoReqArgs;
import com.happy8.args.FindBuddyInfoRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class FindBuddyInfoServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(FindBuddyInfoServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			FindBuddyInfoReqArgs args = null;
			try{
				args = JSON.parseObject(body, FindBuddyInfoReqArgs.class);
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
			
			long id = Happy8DAO.insertFindBuddyInfo(args.getUserId(), args.getInfoContent());
			FindBuddyInfoItem item = Happy8DAO.getFindBuddyInfo(id);
			FindBuddyInfoRspArgs res = new FindBuddyInfoRspArgs();
			res.setFdItem(item);
			res.setBdInfoId(id);
			HttpTools.sendOkResponse(response, JSON.toJSONString(res));
			
		}catch(Exception ex){
			log.error("FindBuddyInfoServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
