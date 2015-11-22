package com.happy8.app.table;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happy8.args.AddTableReqArgs;
import com.happy8.args.AddTableRspArgs;
import com.happy8.args.FindBuddyInfoReqArgs;
import com.happy8.args.FindBuddyInfoRspArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class AddTableServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(AddTableServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			AddTableReqArgs args = null;
			try{
				args = JSON.parseObject(body, AddTableReqArgs.class);
			}catch(Exception ex){
				log.error("parse error req: "+body, ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			long id = Happy8DAO.insertTable(args.getClubId(), args.getType(), args.getTabName(), args.getPrice());
			AddTableRspArgs res = new AddTableRspArgs();
			res.setTableId((int)id);
			HttpTools.sendOkResponse(response, JSON.toJSONString(res,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
			
		}catch(Exception ex){
			log.error("AddTableServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
