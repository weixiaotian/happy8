package com.happy8.app.club;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.happy8.args.AddClubReqArgs;
import com.happy8.args.AddClubRspArgs;
import com.happy8.args.ClubItem;
import com.happy8.args.DeleteClubReqArgs;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.GeoHashTool;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class ProcessClubServlet extends HttpServlet{
	private static Logger log = LoggerFactory.getLogger(ProcessClubServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			String action = request.getParameter("action");
			if(StringUtils.isNullOrEmpty(action)){
				log.error("action param is null");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			if(action.equals("add")){
				processAdd(request, response);
			}else if(action.equals("update")){
				processUpdate(request, response);
			}else if(action.equals("delete")){
				processDelete(request, response);
			}else{
				log.error("action unkonw : " + action);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
			}			
		}catch(Exception ex){
			log.error("ProcessClubServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}

	private void processAdd(HttpServletRequest request,
			HttpServletResponse response) throws Exception, IOException {
		String body = HttpTools.getRequestBody(request);
		AddClubReqArgs args = null;
		try{
			args = JSON.parseObject(body, AddClubReqArgs.class);
		}catch(Exception ex){
			log.error("parse error req: "+body, ex);
			HttpTools.sendResponseOnlyStatusCode(response, 400);
			return;
		}
		
		if(StringUtils.isNullOrEmpty(body) || args == null){
			HttpTools.sendResponseOnlyStatusCode(response, 400);
			return;
		}
		
		if(StringUtils.isNullOrEmpty(args.getOwnerId())){
			log.error("req userid is null");
			HttpTools.sendResponseOnlyStatusCode(response, 400);
			return;
		}
		String geohash = GeoHashTool.generateGeoHashCode(args.getLongitude(), args.getLatitude());
		int id = Happy8DAO.insertClub(args.getOwnerId(), args.getAddr(), args.getPhone(), args.getPalyStyle(),
				args.getSale(), args.getLongitude(), args.getLatitude(), geohash);
		
		AddClubRspArgs res = new AddClubRspArgs();
		res.setClubId(id);
		HttpTools.sendOkResponse(response, JSON.toJSONString(res));
	}
	
	private void processUpdate(HttpServletRequest request,
			HttpServletResponse response) throws Exception, IOException {
		String body = HttpTools.getRequestBody(request);
		ClubItem args = null;
		try{
			args = JSON.parseObject(body, ClubItem.class);
		}catch(Exception ex){
			log.error("parse error req: "+body, ex);
			HttpTools.sendResponseOnlyStatusCode(response, 400);
			return;
		}
		
		if(StringUtils.isNullOrEmpty(body) || args == null){
			HttpTools.sendResponseOnlyStatusCode(response, 400);
			return;
		}
		
		String sqlFormat = "update ha_club set %s where clubid = ?";
		
		StringBuilder sb = new StringBuilder();
		
		List<Object> values = new ArrayList<Object>();
		if(!StringUtils.isNullOrEmpty(args.getAddr())){
			sb.append(", addr = ? ");
			values.add(args.getAddr());
		}
		if(!StringUtils.isNullOrEmpty(args.getOwnerId())){
			sb.append(", ownerid = ? ");
			values.add(args.getOwnerId());
		}
		if(!StringUtils.isNullOrEmpty(args.getPalyStyle())){
			sb.append(", playstyle = ? ");
			values.add(args.getPalyStyle());
		}
		if(!StringUtils.isNullOrEmpty(args.getPhone())){
			sb.append(", phone = ? ");
			values.add(args.getPhone());
		}
		
		if(args.getSale() != 0){
			sb.append(", sale = ? ");
			values.add(args.getSale());
		}
		boolean needGeoHash = false;
		if(args.getLongitude() != 0){
			sb.append(", longitude = ? ");
			values.add(args.getLongitude());
			needGeoHash = true;
		}
		
		if(args.getLatitude() != 0){
			sb.append(", latitude = ? ");
			values.add(args.getLatitude());
			needGeoHash = true;
		}
		
		if(needGeoHash){
			sb.append(", phone = ? ");
			values.add(GeoHashTool.generateGeoHashCode(args.getLongitude(), args.getLatitude()));
		}
		
		if(StringUtils.isNullOrEmpty(sb.toString())){
			log.error("req no filed: "+body);
			HttpTools.sendResponseOnlyStatusCode(response, 400);
			return;
		}
		String strField = sb.toString().indexOf(",") == 0 ? sb.toString().substring(1) : sb.toString();
		values.add(args.getClubId());
		int statusCode = Happy8DAO.updateClub(String.format(sqlFormat, strField), values.toArray());
		HttpTools.sendResponseOnlyStatusCode(response, statusCode);
	}
	
	private void processDelete(HttpServletRequest request,
			HttpServletResponse response) throws Exception, IOException {
		String body = HttpTools.getRequestBody(request);
		DeleteClubReqArgs args = null;
		try{
			args = JSON.parseObject(body, DeleteClubReqArgs.class);
		}catch(Exception ex){
			log.error("parse error req: "+body, ex);
			HttpTools.sendResponseOnlyStatusCode(response, 400);
			return;
		}
		
		if(StringUtils.isNullOrEmpty(body) || args == null){
			HttpTools.sendResponseOnlyStatusCode(response, 400);
			return;
		}
		Happy8DAO.deleteClub(args.getClubId());
		HttpTools.sendResponseOnlyStatusCode(response, 200);
	}
}
