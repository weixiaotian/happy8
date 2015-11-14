package com.happy8.app.table;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.happy8.app.timeline.DeleteTimeLineServlet;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.HttpTools;

public class DeleteTableServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(DeleteTableServlet.class);
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			int tableId = 0;
			try{
				tableId = Integer.parseInt(request.getParameter("tableid"));
			}catch(Exception ex){
				log.error("tableid parse error", ex);
				HttpTools.sendResponseOnlyStatusCode(response, 400);
				return;
			}
			
			Happy8DAO.deleteTable(tableId);
			HttpTools.sendResponseOnlyStatusCode(response, 200);
		}catch(Exception ex){
			log.error("DeleteTableServlet process error",ex);
			HttpTools.sendResponseOnlyStatusCode(response, 500);
		}
	}
}
