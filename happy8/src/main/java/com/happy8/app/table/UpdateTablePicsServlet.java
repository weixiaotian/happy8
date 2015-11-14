package com.happy8.app.table;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.UpdateTableInfoArgs;
import com.happy8.args.UpdateTablePicsReqArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class UpdateTablePicsServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(UpdateTablePicsServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			UpdateTablePicsReqArgs args = null;
			try{
				args = JSON.parseObject(body, UpdateTablePicsReqArgs.class);
			}catch(Exception ex){
				log.error("parse error req: "+body, ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			if(StringUtils.isNullOrEmpty(body) || args == null){
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			if(args.getTableId() == 0){
				log.error("req TableId is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			String sqlFormat = "update ha_table set %s where tableid = ?";
			
			StringBuilder sb = new StringBuilder();
			
			List<Object> values = new ArrayList<Object>();
			if(!StringUtils.isNullOrEmpty(args.getPicUrl())){
				sb.append(", url = ? ");
				values.add(args.getPicUrl());
			}
			
			if(StringUtils.isNullOrEmpty(sb.toString())){
				log.error("req no filed: "+body);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			String strField = sb.toString().indexOf(",") == 0 ? sb.toString().substring(1) : sb.toString();
			values.add(args.getTableId());
			Happy8DAO.updateTableInfo(String.format(sqlFormat, strField), values.toArray());
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("UpdateTablePicsServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
