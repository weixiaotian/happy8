package com.happy8.app.club;

import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.happy8.args.ClubItem;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.GeoHash;
import com.happy8.utils.GeoHashTool;
import com.happy8.utils.HttpTools;
import com.happy8.utils.StringUtils;

public class NearbyClubListServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(NearbyClubListServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			int start = 0;
			int end = 0;
			double longitude;
			double latitude;
			try{
				start = Integer.valueOf(request.getParameter("start"));
				end = Integer.valueOf(request.getParameter("end"));
				longitude = Double.valueOf(request.getParameter("longitude"));
				latitude = Double.valueOf(request.getParameter("latitude"));
			}catch(Exception ex){
				log.error("parse index error",ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			String index = request.getParameter("index");
			if(StringUtils.isNullOrEmpty(index)){
				log.error("index is null ");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			String userId = request.getParameter("userid");
			if(StringUtils.isNullOrEmpty(userId)){
				log.error("userid is null ");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			int geoHashCount = 0;
			if(index.equals("1")){
				geoHashCount = 7;
			}else if(index.equals("2")){
				geoHashCount = 6;
			}else if(index.equals("3")){
				geoHashCount = 5;
			}else{
				log.error("index is null ");
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			String geoHashCode = GeoHashTool.generateGeoHashCode(longitude, latitude).substring(0, geoHashCount);
			GeoHash geoHash = GeoHash.fromGeohashString(geoHashCode);
			String []adjAndCode = geoHash.getAdjacentToString(geoHashCode);
			
			List<ClubItem> res = Happy8DAO.getNearByClubList(adjAndCode, start, end);
			HashSet<Integer> favList = Happy8DAO.getFavoriteClubIds(userId);
			if(favList.size() >0){
				for (ClubItem item : res) {
					if(favList.contains(item.getClubId())){
						item.setMyFavorite(true);
					}
				}
			}
			
			HttpTools.sendOkResponse(response, JSON.toJSONString(res,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
		}catch(Exception ex){
			log.error("NearbyClubListServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
