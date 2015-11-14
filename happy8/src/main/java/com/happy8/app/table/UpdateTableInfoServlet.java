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
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class UpdateTableInfoServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(UpdateTableInfoServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String body = HttpTools.getRequestBody(request);
			UpdateTableInfoArgs args = null;
			try{
				args = JSON.parseObject(body, UpdateTableInfoArgs.class);
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
			if(!StringUtils.isNullOrEmpty(args.getTabName())){
				sb.append(", tablename = ? ");
				values.add(args.getTabName());
			}
			if(args.getClubId()!=0){
				sb.append(", clubid = ? ");
				values.add(args.getClubId());
			}
			if(args.getType() != 0){
				sb.append(", type = ? ");
				values.add(args.getType());
			}
			if(args.getPrice() != 0){
				sb.append(", price = ? ");
				values.add(args.getPrice());
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
			log.error("UpdateTableInfoServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
