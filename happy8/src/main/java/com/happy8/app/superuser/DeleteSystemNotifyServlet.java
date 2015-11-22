package com.happy8.app.superuser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;

public class DeleteSystemNotifyServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(DeleteSystemNotifyServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			String userId = request.getParameter("userid");
			String snIds = request.getParameter("snids");
			if(!Happy8DAO.isUserSuperAdmin(userId)){
				HttpTools.sendResponseOnlyStatusCode(response, 405);
				return;
			}
			String deleteSql = String .format("delete from ha_systemnotify where id in (%s)",snIds);
			Happy8DAO.deleteSystemNotify(deleteSql);
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("DeleteSystemNotifyServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
