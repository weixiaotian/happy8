package com.happy8.app.timeline;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;

public class DeleteTimeLineServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(DeleteTimeLineServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			String userId = request.getParameter("userid");
			long tlInfoId = Long.parseLong(request.getParameter("tlinfoid"));
			//List<FindBuddyInfoItem> res = Happy8DAO.getFindBuddyInfoList(start, end);
			int res = Happy8DAO.deleteTimeLine(tlInfoId);
			if(res > 0){
				List<String> delIds = Happy8DAO.getFriendUserIdList(userId);
				delIds.add(userId);
				for(String delId : delIds){
					Happy8DAO.deleteUserTimeLine(delId, tlInfoId);
				}
			}
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("DeleteTimeLineServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
