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

import com.happy8.app.bookclub.BookClubListServlet;
import com.happy8.app.bookclub.OrderTableServlet;
import com.happy8.app.bookclub.CancelBookServlet;
import com.happy8.app.bookclub.QueryTablesServlet;
import com.happy8.app.bookclub.UpdateOrderStatusServlet;
import com.happy8.app.club.FavoriteClubListServlet;
import com.happy8.app.club.MyOwnClubListServlet;
import com.happy8.app.club.NearbyClubListServlet;
import com.happy8.app.club.ProcessClubServlet;
import com.happy8.app.club.ProcessFavoriteServlet;
import com.happy8.app.club.QueryClubListServlet;
import com.happy8.app.club.RateClubServlet;
import com.happy8.app.club.UnApproveClubListServlet;
import com.happy8.app.coupon.AddCouponServlet;
import com.happy8.app.coupon.ConsumeCouponServlet;
import com.happy8.app.coupon.DeleteCouponServlet;
import com.happy8.app.coupon.MyCouponListServlet;
import com.happy8.app.findbuddy.FindBuddyInfoListServlet;
import com.happy8.app.findbuddy.FindBuddyInfoServlet;
import com.happy8.app.findbuddy.GetFindBuddyInfoServlet;
import com.happy8.app.findbuddy.ReplayFindBuddyInfoServlet;
import com.happy8.app.friend.AddFriendServlet;
import com.happy8.app.friend.AllowAddFriendServlet;
import com.happy8.app.friend.DeleteFriendServlet;
import com.happy8.app.superuser.ApproveClubServlet;
import com.happy8.app.superuser.DeleteFindBuddyInfoServlet;
import com.happy8.app.superuser.DeleteFindBuddyReplyServlet;
import com.happy8.app.superuser.DeleteSystemNotifyServlet;
import com.happy8.app.superuser.GetSystemNotifyServlet;
import com.happy8.app.superuser.PublishSystemNotifyServlet;
import com.happy8.app.table.AddTableServlet;
import com.happy8.app.table.DeleteTableServlet;
import com.happy8.app.table.UpdateTableInfoServlet;
import com.happy8.app.table.UpdateTablePicsServlet;
import com.happy8.app.timeline.DeleteTimeLineServlet;
import com.happy8.app.timeline.ReplayTimeLineServlet;
import com.happy8.app.timeline.TimeLineInfoListServlet;
import com.happy8.app.timeline.TimeLineInfoServlet;
import com.happy8.app.user.GetUserInfoServlet;
import com.happy8.app.user.LogoutServlet;
import com.happy8.app.user.QueryUserLevelServlet;
import com.happy8.app.user.RegisterUserServlet;
import com.happy8.app.user.ResetPasswordServlet;
import com.happy8.app.user.UpdateUserInfoServlet;
import com.happy8.app.user.UserLoginServlet;
import com.happy8.dao.Happy8DAO;
import com.happy8.utils.Happy8Config;

public class Happy8HttpService {
public static final Logger LOGGER = LoggerFactory.getLogger(Happy8HttpService.class);
	
	public void start() throws Exception {
		// TODO Auto-generated method stub
		try{
			init();
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("HttpService initializing");
			}
			Happy8Config.init();
			intWebService();
			SystemNotifySender.start();
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
		int port=Happy8Config.Port;
		Server server = new Server(port);
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(250);
		threadPool.setMinThreads(10);
		server.setThreadPool(threadPool);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(new UserLoginServlet()), "/happy8/userlogin");
		context.addServlet(new ServletHolder(new RegisterUserServlet()), "/happy8/registeruser");
		context.addServlet(new ServletHolder(new ResetPasswordServlet()), "/happy8/resetpassword");
		context.addServlet(new ServletHolder(new UpdateUserInfoServlet()), "/happy8/setuserinfo");
		context.addServlet(new ServletHolder(new GetUserInfoServlet()), "/happy8/getuserinfo");
		context.addServlet(new ServletHolder(new LogoutServlet()), "/happy8/logout");
		
		context.addServlet(new ServletHolder(new FindBuddyInfoServlet()), "/happy8/findbuddyinfo");
		context.addServlet(new ServletHolder(new FindBuddyInfoListServlet()), "/happy8/findbuddyinfolist");
		context.addServlet(new ServletHolder(new ReplayFindBuddyInfoServlet()), "/happy8/replayfindbuddy");
		
		context.addServlet(new ServletHolder(new TimeLineInfoServlet()), "/happy8/timeLineinfo");
		context.addServlet(new ServletHolder(new TimeLineInfoListServlet()), "/happy8/timelineinfolist");
		context.addServlet(new ServletHolder(new ReplayTimeLineServlet()), "/happy8/replaytimeline");
		context.addServlet(new ServletHolder(new DeleteTimeLineServlet()), "/happy8/deletetimeline");
		
		context.addServlet(new ServletHolder(new ProcessClubServlet()), "/happy8/processclub");
		context.addServlet(new ServletHolder(new ProcessFavoriteServlet()), "/happy8/processfavorite");
		context.addServlet(new ServletHolder(new FavoriteClubListServlet()), "/happy8/favoriteclublist");
		context.addServlet(new ServletHolder(new QueryClubListServlet()), "/happy8/queryclublist");
		context.addServlet(new ServletHolder(new NearbyClubListServlet()), "/happy8/nearbyclublist");
		
		context.addServlet(new ServletHolder(new AddFriendServlet()), "/happy8/addfriend");
		context.addServlet(new ServletHolder(new DeleteFriendServlet()), "/happy8/deletefriend");
		context.addServlet(new ServletHolder(new AllowAddFriendServlet()), "/happy8/allowaddfriend");
		
		context.addServlet(new ServletHolder(new OrderTableServlet()), "/happy8/bookclub");
		context.addServlet(new ServletHolder(new CancelBookServlet()), "/happy8/cancelbook");
		context.addServlet(new ServletHolder(new BookClubListServlet()), "/happy8/bookclublist");
		
		context.addServlet(new ServletHolder(new QueryUserLevelServlet()), "/happy8/queryuserlevel");
		context.addServlet(new ServletHolder(new RateClubServlet()), "/happy8/rateclub");
		
		context.addServlet(new ServletHolder(new ApproveClubServlet()), "/happy8/approveclub");
		context.addServlet(new ServletHolder(new DeleteFindBuddyInfoServlet()), "/happy8/deletefindbuddyinfo");
		context.addServlet(new ServletHolder(new DeleteFindBuddyReplyServlet()), "/happy8/deletefindbuddyreply");
		
		context.addServlet(new ServletHolder(new AddCouponServlet()), "/happy8/addcoupon");
		context.addServlet(new ServletHolder(new DeleteCouponServlet()), "/happy8/deletecoupon");
		context.addServlet(new ServletHolder(new ConsumeCouponServlet()), "/happy8/consumecoupon");
		context.addServlet(new ServletHolder(new MyCouponListServlet()), "/happy8/mycouponlist");
		
		context.addServlet(new ServletHolder(new AddTableServlet()), "/happy8/addtable");
		context.addServlet(new ServletHolder(new UpdateTablePicsServlet()), "/happy8/updatetablepics");
		context.addServlet(new ServletHolder(new UpdateTableInfoServlet()), "/happy8/updatetableinfo");
		context.addServlet(new ServletHolder(new DeleteTableServlet()), "/happy8/deletetable");
		context.addServlet(new ServletHolder(new MyOwnClubListServlet()), "/happy8/myownclublist");
		context.addServlet(new ServletHolder(new GetFindBuddyInfoServlet()), "/happy8/getfindbuddyinfo");
		
		context.addServlet(new ServletHolder(new OrderTableServlet()), "/happy8/ordertable");
		context.addServlet(new ServletHolder(new QueryTablesServlet()), "/happy8/querytables");
		
		context.addServlet(new ServletHolder(new UnApproveClubListServlet()), "/happy8/unapproveclub");
		
		context.addServlet(new ServletHolder(new DeleteSystemNotifyServlet()), "/happy8/deletesystemnotify");
		context.addServlet(new ServletHolder(new GetSystemNotifyServlet()), "/happy8/getsystemnotify");
		context.addServlet(new ServletHolder(new PublishSystemNotifyServlet()), "/happy8/publishsystemnotify");
		context.addServlet(new ServletHolder(new UpdateOrderStatusServlet()), "/happy8/updateorderstatus");
		
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
