package com.happy8;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.happy8.app.GetUserInfoServlet;
import com.happy8.app.UserLoginServlet;
import com.happy8.dao.Happy8DAO;

public class Happy8HttpService {
public static final Logger LOGGER = LoggerFactory.getLogger(Happy8HttpService.class);
	
	public void start() throws Exception {
		// TODO Auto-generated method stub
		try{
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("HttpService initializing");
			}
			init();
			intWebService();
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("FriendCircleWebService start ok");
			}
		}catch (Exception e) {
			LOGGER.error("start FriendCircleWebService error:", e);
			throw e;
		}
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("FriendCircleWebService start success");
		}
	}

	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	private void init() throws Exception{
		PropertyConfigurator.configure("log4j.properties");
		Happy8DAO.initialize();
	}
	
	private void intWebService()throws Exception{
		//int port = ServiceSettings.INSTANCE.getServicePort("web_http");
		int port=8080;
		Server server = new Server(port);
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(250);
		threadPool.setMinThreads(10);
		server.setThreadPool(threadPool);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(new GetUserInfoServlet()), "/happy8/getuserinfo");
		context.addServlet(new ServletHolder(new UserLoginServlet()), "/happy8/userlogin");
		
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		connector.setAcceptors(4);
		connector.setAcceptQueueSize(2500);
		connector.setThreadPool(new QueuedThreadPool(100));
		connector.setName("JettyHttpServer");
		connector.setLowResourcesConnections(10240);
		server.setConnectors(new Connector[] { connector });
		
		server.start();
	}
}
