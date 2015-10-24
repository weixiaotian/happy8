package com.happy8.app.bookclub;

import java.sql.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.app.friend.AddFriendServlet;
import com.happy8.args.AddFriendReqArgs;
import com.happy8.args.BookClubReqArgs;
import com.happy8.args.BookClubRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class BookClubServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(BookClubServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			BookClubReqArgs args = null;
			try{
				args = JSON.parseObject(body, BookClubReqArgs.class);
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
			
			long res = Happy8DAO.insertBookClub(args.getUserId(), args.getClubId(), args.getTableIndex()
					, args.getChairIndex(), Date.valueOf(args.getStartTime()), args.getDuration());
			
			BookClubRspArgs resArgs = new BookClubRspArgs();
			resArgs.setBookId(res);
			HttpTools.sendOkResponse(response, JSON.toJSONString(resArgs));
		}catch(Exception ex){
			log.error("BookClubServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
