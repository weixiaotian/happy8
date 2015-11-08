package com.happy8.app.superuser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;

public class DeleteFindBuddyInfoServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(DeleteFindBuddyInfoServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			String userId = request.getParameter("userid");
			long fdinfoid = Long.parseLong(request.getParameter("fdinfoid"));
			
			if(!Happy8DAO.isUserSuperAdmin(userId)){
				HttpTools.sendResponseOnlyStatusCode(response, 405);
				return;
			}
			Happy8DAO.deleteFindBuddyInfo(fdinfoid);;
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("DeleteFindBuddyInfoServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
