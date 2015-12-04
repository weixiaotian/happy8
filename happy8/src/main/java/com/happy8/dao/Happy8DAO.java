package com.happy8.dao;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tian.database.DataRow;
import tian.database.DataTable;
import tian.database.Database;
import tian.database.DatabaseManager;

import com.happy8.app.findbuddy.CheckOrderItem;
import com.happy8.args.BookClubItem;
import com.happy8.args.ClubItem;
import com.happy8.args.CouponItem;
import com.happy8.args.FindBuddyCommentInfo;
import com.happy8.args.FindBuddyInfoItem;
import com.happy8.args.OrderItem;
import com.happy8.args.QueryTableItem;
import com.happy8.args.SystemNotifyItem;
import com.happy8.args.TimeLineCommentInfoItem;
import com.happy8.args.TimeLineInfoItem;
import com.happy8.args.UserInfoArgs;
import com.happy8.args.UserLevelArgs;
import com.happy8.utils.StringUtils;

public class Happy8DAO {
	private static Logger log = LoggerFactory.getLogger(Happy8DAO.class);
	private static Database happy8DB = null;
	
	private static String sqlSelectUserInfo = "select userid,gender,signature,brief,avatarurl from ha_user where userid = ?";
	private static String sqlUserLogin = "select userid,password from ha_user where userid = ?";
	private static String sqlRegisterUser = "insert into ha_user(userid,password) values(?,?)";
	private static String sqlResetPassword = "update ha_user set password = ? where userid = ?";
	private static String sqlSelectFindBuddyInfoList = "select a.bdinfoid,a.userid,a.infocontent,a.createdate,b.signature,b.avatarurl  from ha_findbuddyinfo a,ha_user b where a.userid = b.userid order by a.bdinfoid desc limit ?,?";
	private static String sqlSelectFindBuddyInfo = "select a.bdinfoid,a.userid,a.infocontent,a.createdate,b.signature,b.avatarurl  from ha_findbuddyinfo a,ha_user b where a.userid = b.userid and a.bdinfoid = ?";
	private static String sqlSelectFindBuddyCommentList = "SELECT a.commentid,a.bdinfoid,a.publishid,a.commentedid,a.commenttext,a.createdate,b.signature,b.avatarurl FROM ha_findbuddycomment a,ha_user b WHERE a.publishid = b.userid and bdinfoid IN (%s) order by a.commentid desc";
	private static String sqlSelectTimeLineCommentList = "SELECT commentid,tlinfoid,publishid,commentedid,commenttext FROM ha_timelinecomment WHERE tlinfoid IN (%s)";
	private static String sqlSelectFriendList = "select friendid from ha_friend where userid = ?";
	private static String sqlInsertUserTimeLine = "insert into ha_usertimeline values(?,?)";
	private static String sqlDeleteTimeLine = "delete from ha_usertimeline where tlinfoid = ?";
	private static String sqlDeleteUserTimeLine = "delete from ha_usertimeline where userid = ? and tlinfoid = ?";
	private static String sqlSelectTimeLineSelfSendList = "select tlinfoid,userid,infocontent from ha_timeline where userid = ? order by tlinfoid desc limit ?,?";
	private static String sqlSelectTimeLineList = "select b.tlinfoid,b.userid,b.infocontent from ha_usertimeline a,ha_timeline b where a.userid = ? and a.tlinfoid = b.tlinfoid order by a.tlinfoid desc limit ?,?";
	private static String sqlDeleteClub = "delete from ha_club where clubid = ?";
	private static String sqlInsertFavoriteClub = "insert into ha_userfavoriteclub(userid,clubid) values(?,?)";
	private static String sqlDeleteFavoriteClub = "delete from ha_userfavoriteclub where userid = ? and clubid = ?";
	private static String sqlSelectFavoriteClubList = "select b.clubid,b.ownerid,b.name,b.addr,b.sale,b.phone,b.playstyle,b.longitude,b.status,b.latitude,b.clubimageurl from ha_userfavoriteclub a,ha_club b where a.userid = ? and a.clubid = b.clubid order by a.clubid desc limit ?,?";
	private static String sqlSelectUnApproveClubList = "select clubid,ownerid,name,addr,sale,phone,playstyle,longitude,latitude,clubimageurl from ha_club where status = 0 order by clubid desc limit ?,?";
	private static String sqlQueryClubListByTel = "select clubid,ownerid,name,addr,sale,phone,playstyle,longitude,latitude,status,clubimageurl from ha_club where phone like ? and status = 1 order by clubid desc limit ?,?";
	private static String sqlSelectMyOwnClubList = "select clubid,ownerid,name,addr,sale,phone,playstyle,longitude,latitude,status,clubimageurl,status from ha_club where ownerid = ? order by clubid desc limit ?,?";
	private static String sqlQueryClubListByAddr = "select clubid,ownerid,name,addr,sale,phone,playstyle,longitude,latitude,status,clubimageurl from ha_club where addr like ? and status = 1 order by clubid desc limit ?,?";
	private static String sqlSelectClubItem = "select clubid,ownerid,name,addr,sale,phone,playstyle,longitude,latitude,status,clubimageurl from ha_club where clubid = ?";
	private static String sqlQueryClubListByGeoHash = "select clubid,ownerid,name,addr,sale,phone,playstyle,longitude,latitude,status,clubimageurl from ha_club where (geohash like ? or geohash like ? or geohash like ? or geohash like ? or geohash like ? or geohash like ? or geohash like ? or geohash like ? or geohash like ?) and status = 1  order by clubid desc limit ?,?";
	private static String sqlInsertAddFriendReq = "insert into ha_addfriendreq(userid,friendid) values(?,?)";
	private static String sqlDeleteAddFriendReq = "delete from ha_addfriendreq where userid = ? and friendid = ?";
	private static String sqlInsetFriendReq = "insert into ha_friend(userid,friendid) values(?,?)";
	private static String sqlDeleteFriend = "delete from ha_friend where userid = ? and friendid = ?";
	private static String sqlDeleteOrderById = "delete from ha_order where orderid = ?";
	//private static String sqlSelectBookClubList = "select bookid,userid,clubid,tableindex,chairindex,starttime,duration from ha_bookclub where userid = ? order by bookid desc limit ?,?";
	private static String sqlSelectOrderList = "select a.orderid,a.userid,b.tableid,b.tablename,c.clubid,c.name,a.date,a.gametime,d.paystatus from ha_orderdetail a,ha_order d,ha_table b,ha_club c where a.orderid = d.orderid and a.tableid = b.tableid and b.clubid = c.clubid and a.userid = ? and (a.paystatus = 1 or a.createdate > ?) order by a.orderid desc limit ?,?";
	private static String sqlSelectTablesByClubId = "select a.tableid,a.tablename,a.price,a.type,a.url from ha_table a where a.clubid = ? order by a.clubid desc limit ?,?";
	
	private static String sqlIsUserSuperAdimin = "select userid from ha_user where userid = ? and usertype = 4";
	private static String sqlInsertCoupon = "insert into ha_coupon(couponid,type,discount,value,startamout,expiretime) values (?,?,?,?,?,?)";
	private static String sqlInsertUserCoupon = "insert into ha_usercoupon(userid,consumecouponid,createdate) values(?,?,?) ON DUPLICATE KEY UPDATE createdate = ?";
	private static String sqlDeleteCoupon= "delete from ha_coupon where couponid = ?";
	private static String sqlSelectMyCouponList = "SELECT a.couponid,a.type,a.discount,a.value,a.startamout,b.consumecouponid FROM ha_coupon a LEFT JOIN ha_usercoupon b ON a.`couponid` = b.`consumecouponid` WHERE  b.userid = ? ORDER BY a.id DESC LIMIT ?,?";
	private static String sqlSelectUnConsumeCouponList = "select couponid,type,discount,value,startamout from ha_coupon where expiretime > ? ORDER BY id DESC LIMIT ?,?";
	private static String sqlSelectConsumeCouponList = "select a.couponid,a.type,a.discount,a.value,a.startamout from ha_coupon a,ha_usercoupon b where a.`couponid` = b.`consumecouponid`  ORDER BY a.id DESC LIMIT ?,? ";
	private static String sqlUpdateClubStatus = "UPDATE ha_club SET status = ? WHERE clubid = ?";
	private static String sqlDeleteFindBuddyInfo = "delete from ha_findbuddyinfo where bdinfoid = ?";
	private static String sqlDeleteFindBuddyReplayAllOne = "delete from ha_findbuddycomment where bdinfoid = ?";
	private static String sqlDeleteFindBuddyReplay = "delete from ha_findbuddycomment where commentid = ?";
	private static String sqlSelectFrindBuddyReplay = "select a.commentid,a.bdinfoid,a.publishid,a.commentedid,a.commenttext,a.createdate from ha_findbuddycomment a where a.commentid = ?";
	private static String sqlInsertUserRate = "insert into ha_userclubrate(clubid,userid,rate) values(?,?,?)  ON DUPLICATE KEY UPDATE  rate = ?";
	private static String sqlSelectSumRateAndCount = "select sum(rate) sum,count(*) count from ha_userclubrate where clubid = ?";
	private static String sqlUpdateClubRate = "update ha_club set rate = ? where clubid = ?";
	private static String sqlSelectUserLevel = "select usertype,userstatus from ha_user where userid = ?";
	private static String sqlUpdateUserStatus = "update ha_user set userstatus = ?,pushtoken = ? where userid = ?";
	private static String sqlDeleteTable = "delete from ha_table where tableid = ?";
	private static String sqlSelectOrderByDate = "select a.orderid, a.paystatus,a.createdate from ha_order a,ha_orderdetail b where a.orderid = b.orderid and  b.tableid = ? and b.date = ? and b.gametime = ?";
	private static String sqlDeleteNoPayOrder = "delete from ha_order where orderid = ?";
	private static String sqlInsertOrderDetail = "insert into ha_orderdetail(orderid,userid,tableid,date,gametime) values(?,?,?,?,?)";
	private static String sqlDeleteOrderDetail = "delete from ha_orderdetail where orderid = ?";
	private static String sqlSelectOrderStatus = "select a.userid,a.createdate,c.paystatus,b.signature from ha_orderdetail a, ha_order c,ha_user b where a.orderid = c.orderid and a.userid = b.userid and a.tableid = ? and date = ? and gametime = ?";
	private static String sqlSelectSystemNotify = "select id,title,content,sendtime from ha_systemnotify order by sendtime LIMIT ?,?";
	private static String sqlSelectUnSendNotify = "select id,title,content,sendtime from ha_systemnotify where sendflag = 0 and sendtime < ? ";
	private static String sqlSelectPushToken = "select pushtoken from ha_user where userid = ?";
	 
	public static void initialize() throws Exception{
		Properties p = new Properties();
		p.load(new FileInputStream("happy8db.properties"));
		happy8DB = DatabaseManager.getDatabase("happy8DB", p);
	}
	
	public static UserInfoArgs getUserInfo(String userId) throws Exception{
		try{
			UserInfoArgs args = new UserInfoArgs();
			Object []values = { userId };
			DataTable dt = happy8DB.executeTable(sqlSelectUserInfo, values);
			if(dt.getRowCount() == 0){
				return null;
			}
			args.setUserId(dt.getRow(0).getString("userid"));
			args.setAvatarUrl(dt.getRow(0).getString("avatarurl"));
			args.setBrief(dt.getRow(0).getString("brief"));
			args.setGender(dt.getRow(0).getInt("gender"));
			args.setSignature(dt.getRow(0).getString("signature"));
			return args;
		}catch(Exception ex){
			log.error("getUserInfo error", ex);
			throw ex;
		}
		
	}
	
	public static String getUserPushToken(String userId) throws SQLException{
		Object []values = { userId };
		DataTable dt = happy8DB.executeTable(sqlSelectPushToken, values);
		if(dt.getRowCount() == 0){
			return "";
		}
		
		return dt.getRow(0).getString("pushtoken");
	}
	
	public static int userLogin(String userId,String password) throws Exception{
		try{
			Object []values = { userId };
			DataTable dt = happy8DB.executeTable(sqlUserLogin, values);
			if(dt.getRowCount() == 0){
				return 404;
			}
			String dbPassword = dt.getRow(0).getString("password");
			if(password.equals(dbPassword)){
				return 200;
			}
			log.info(String.format("db pwd is %s req pwd is %s", dbPassword,password));
			return 401;
		}catch(Exception ex){
			log.error("userLogin error", ex);
			throw ex;
		}
	}
	
	public static void registerUser(String userId,String password) throws Exception{
		try{
			Object []values = { userId,password };
			happy8DB.executeNonQuery(sqlRegisterUser, values);
		}catch(Exception ex){
			log.error("registerUser error", ex);
			throw ex;
		}
	}
	
	public static int resetPassword(String userId,String password) throws Exception{
		try{
			Object []values = { password, userId};
			int res = happy8DB.executeNonQuery(sqlResetPassword, values);
			if(res > 0)
				return 200;
			log.error("resetPassword not found userid :"+userId);
			return 404;
		}catch(Exception ex){
			log.error("resetPassword error", ex);
			throw ex;
		}
	}
	
	public static int updateUserInfo(String sql,Object []values) throws Exception{
		try{
			int res = happy8DB.executeNonQuery(sql, values);
			if(res > 0)
				return 200;
			return 404;
		}catch(Exception ex){
			log.error("updateUserInfo error", ex);
			throw ex;
		}
	}
	
	public static void deleteSystemNotify(String sql) throws Exception{
		try{
			happy8DB.executeNonQuery(sql);
		}catch(Exception ex){
			log.error("deleteSystemNotify error", ex);
			throw ex;
		}
	}
	
	public static void updateTableInfo(String sql,Object []values) throws Exception{
		try{
			happy8DB.executeNonQuery(sql, values);
		}catch(Exception ex){
			log.error("updateTableInfo error", ex);
			throw ex;
		}
	}
	
	public static long insertFindBuddyInfo(String userId,String infoContent) throws Exception{
		try{
			String []params = {"@userid","@infocontent"};
			Object []values = {userId,infoContent};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertFindBuddyInfo", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertFindBuddyInfo error", ex);
			throw ex;
		}
	}
	
	public static int insertSystemNotify(String title,String content,Date sendTime) throws Exception{
		try{
			String []params = {"@title","@content","@sendtime"};
			Object []values = {title,content,sendTime};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertSystemNotify", params, values);
			return dt.getRow(0).getInt(1);
		}catch(Exception ex){
			log.error("insertSystemNotify error", ex);
			throw ex;
		}
	}
	
	public static long insertTable(int clubId,int type,String tableName,float price) throws Exception{
		try{
			String []params = {"@clubid","@type","@tablename","@price"};
			Object []values = {clubId,type,tableName,price};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertTable", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertTable error", ex);
			throw ex;
		}
	}
	
	public static long insertFindBuddyComment(long bdInfoId,String publishId,String commentedId,String commentText) throws Exception{
		try{
			String []params = {"@bdinfoid", "@publishid","@commentedid","@commenttext"};
			Object []values = {bdInfoId,publishId,commentedId,commentText};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertFindBuddyComment", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertFindBuddyComment error", ex);
			throw ex;
		}
	}
	
	public static List<FindBuddyInfoItem> getFindBuddyInfoList(int start,int end) throws Exception{
		try{
			List<FindBuddyInfoItem> res = getFindBuddyInfoListNoComment(start,end);
			if(res.size() == 0){
				return res;
			}
			wrapFindBuddyInfoListWithComment(res);
			return res;
		}catch(Exception ex){
			log.error("getFindBuddyInfoList error", ex);
			throw ex;
		}
	}
	
	public static FindBuddyInfoItem getFindBuddyInfo(long fdInfoId) throws Exception{
		try{
			Object []values = { fdInfoId };
			DataTable dt = happy8DB.executeTable(sqlSelectFindBuddyInfo, values);
			FindBuddyInfoItem item = new FindBuddyInfoItem();
			if(dt.getRowCount() > 0){
				DataRow dr = dt.getRow(0);
				item.setBdInfoId(dr.getLong("bdinfoid"));
				item.setUserId(dr.getString("userid"));
				item.setInfoContent(dr.getString("infocontent"));
				item.setAvatarUrl(dr.getString("avatarurl"));
				item.setSignature(dr.getString("signature"));
				item.setDateTime(dateFormat(dr.getDateTime("createdate")));
			}
			List<FindBuddyInfoItem> list = new ArrayList<FindBuddyInfoItem>();
			list.add(item);
			wrapFindBuddyInfoListWithComment(list);
			return item;
		}catch(Exception ex){
			log.error("getFindBuddyInfo error", ex);
			throw ex;
		}
	}
	
	private static List<FindBuddyInfoItem> getFindBuddyInfoListNoComment(int start,int end) throws Exception{
		try{
			List<FindBuddyInfoItem> res = new ArrayList<FindBuddyInfoItem>();
			int count = end - start ;
			Object []values = {start , count};
			DataTable dt = happy8DB.executeTable(sqlSelectFindBuddyInfoList, values);
			for(DataRow dr : dt.getRows()){
				FindBuddyInfoItem item = new FindBuddyInfoItem();
				item.setBdInfoId(dr.getLong("bdinfoid"));
				item.setUserId(dr.getString("userid"));
				item.setInfoContent(dr.getString("infocontent"));
				item.setAvatarUrl(dr.getString("avatarurl"));
				item.setSignature(dr.getString("signature"));
				item.setDateTime(dateFormat(dr.getDateTime("createdate")));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getFindBuddyInfoListNoComment error", ex);
			throw ex;
		}
	}
	
	private static void wrapFindBuddyInfoListWithComment(List<FindBuddyInfoItem> res) throws Exception{
		List<Long> infoIds =new ArrayList<Long>();
		for(FindBuddyInfoItem info : res){
			infoIds.add(info.getBdInfoId());
		}
		
		try{
			DataTable dt = happy8DB.executeTable(String.format(sqlSelectFindBuddyCommentList, joinArrayItem(infoIds)),new Object[0]);
			Map<Long,List<FindBuddyCommentInfo>> map = new HashMap<Long,List<FindBuddyCommentInfo>>();
			for(DataRow dr : dt.getRows()){
				long bdInfoId = dr.getLong("bdinfoid");
				long commentId = dr.getLong("commentid");
				String publishId = dr.getString("publishid");
				String commentText = dr.getString("commenttext");
				
				FindBuddyCommentInfo item = new FindBuddyCommentInfo();
				item.setBdInfoId(bdInfoId);
				item.setCommentId(commentId);
				item.setCommentText(commentText);
				item.setPublishUserId(publishId);
				item.setAvatarUrl(dr.getString("avatarurl"));
				item.setSignature(dr.getString("signature"));
				item.setDateTime(dateFormat(dr.getDateTime("createdate")));
				
				if(map.containsKey(bdInfoId)){
					List<FindBuddyCommentInfo> list = map.get(bdInfoId);
					list.add(item);
				}else{
					List<FindBuddyCommentInfo> list = new ArrayList<FindBuddyCommentInfo>();
					list.add(item);
					map.put(bdInfoId, list);
				}
			}
			for(FindBuddyInfoItem info : res){
				if(map.containsKey(info.getBdInfoId())){
					info.setCommentList(map.get(info.getBdInfoId()));
				}
			}
		}catch(Exception ex){
			log.error("wrapFindBuddyInfoListWithComment error", ex);
			throw ex;
		}
	}
	
	public static List<String> getFriendUserIdList(String userId) throws Exception{
		List<String> res = new ArrayList<String>();
		try{
			Object []values = {userId};
			DataTable dt = happy8DB.executeTable(sqlSelectFriendList, values);
			for(DataRow dr : dt.getRows()){
				res.add(dr.getString("friendid"));
			}
			return res;
		}catch(Exception ex){
			log.error("insertTimeLineInfo error", ex);
			throw ex;
		}
	}
	
	public static long insertTimeLineInfo(String userId,String infoContent) throws Exception{
		try{
			String []params = {"@userid","@infocontent"};
			Object []values = {userId,infoContent};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertTimeLineInfo", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertTimeLineInfo error", ex);
			throw ex;
		}
	}
	
	public static void insertUserTimeLine(String userId,long tlInfoId) throws Exception{
		try{
			Object []values = {userId,tlInfoId};
			happy8DB.executeNonQuery(sqlInsertUserTimeLine, values);
		}catch(Exception ex){
			log.error("insertTimeLineInfo error", ex);
			throw ex;
		}
	}
	
	public static long insertTimeLineComment(long tlInfoId,String publishId,String commentedId,String commentText) throws Exception{
		try{
			String []params = {"@tlinfoid", "@publishid","@commentedid","@commenttext"};
			Object []values = {tlInfoId,publishId,commentedId,commentText};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertTimeLineComment", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertFindBuddyComment error", ex);
			throw ex;
		}
	}
	
	public static int deleteTimeLine(long tlInfoId) throws Exception{
		try{
			Object []values = { tlInfoId };
			return happy8DB.executeNonQuery(sqlDeleteTimeLine, values);
		}catch(Exception ex){
			log.error("deleteTimeLine error", ex);
			throw ex;
		}
	}
	
	public static void deleteTable(int tableId) throws Exception{
		try{
			Object []values = { tableId };
			happy8DB.executeNonQuery(sqlDeleteTable, values);
		}catch(Exception ex){
			log.error("deleteTable error", ex);
			throw ex;
		}
	}
	
	public static void deleteUserTimeLine(String userId,long tlInfoId) throws Exception{
		try{
			Object []values = { userId,tlInfoId };
			happy8DB.executeNonQuery(sqlDeleteUserTimeLine, values);
		}catch(Exception ex){
			log.error("deleteUserTimeLine error", ex);
			throw ex;
		}
	}
	
	public static List<TimeLineInfoItem> getTimeLineList(String userId,int start,int end) throws Exception{
		try{
			List<TimeLineInfoItem> res = new ArrayList<TimeLineInfoItem>();
			int count = end - start;
			Object []values = {userId,start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectTimeLineList, values);
			for(DataRow dr : dt.getRows()){
				TimeLineInfoItem item = new TimeLineInfoItem();
				item.setUserId(dr.getString("userid"));
				item.setTlInfoId(dr.getLong("tlinfoid"));
				item.setInfoContent(dr.getString("infocontent"));
				res.add(item);
			}
			if(res.size() > 0){
				wrapTimeLineInfoListWithComment(res);
			}
			return res;
		}catch(Exception ex){
			log.error("getTimeLineList error", ex);
			throw ex;
		}
	}
	
	public static List<TimeLineInfoItem> getTimeLineSelfSendList(String userId,int start,int end) throws Exception{
		try{
			List<TimeLineInfoItem> res = new ArrayList<TimeLineInfoItem>();
			int count = end - start;
			Object []values = {userId,start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectTimeLineSelfSendList, values);
			for(DataRow dr : dt.getRows()){
				TimeLineInfoItem item = new TimeLineInfoItem();
				item.setUserId(dr.getString("userid"));
				item.setTlInfoId(dr.getLong("tlinfoid"));
				item.setInfoContent(dr.getString("infocontent"));
				res.add(item);
			}
			if(res.size() > 0){
				wrapTimeLineInfoListWithComment(res);
			}
			return res;
		}catch(Exception ex){
			log.error("getTimeLineSelfSendList error", ex);
			throw ex;
		}
	}
	
	private static String joinArrayItem(List<Long> ids){
		if(ids == null || ids.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for(Long id : ids){
			sb.append(String.valueOf(id));
			sb.append(",");
		}
		return sb.substring(0, sb.length()-1);
	}
	
	private static void wrapTimeLineInfoListWithComment(List<TimeLineInfoItem> res) throws Exception{
		List<Long> infoIds =new ArrayList<Long>();
		for(TimeLineInfoItem info : res){
			infoIds.add(info.getTlInfoId());
		}
		
		try{
			DataTable dt = happy8DB.executeTable(String.format(sqlSelectTimeLineCommentList, joinArrayItem(infoIds)),new Object[0]);
			Map<Long,List<TimeLineCommentInfoItem>> map = new HashMap<Long,List<TimeLineCommentInfoItem>>();
			for(DataRow dr : dt.getRows()){
				long tlInfoId = dr.getLong("tlinfoid");
				long commentId = dr.getLong("commentid");
				String publishId = dr.getString("publishid");
				String commentedId = dr.getString("commentedid");
				String commentText = dr.getString("commenttext");
				
				TimeLineCommentInfoItem item = new TimeLineCommentInfoItem();
				item.setTlInfoId(tlInfoId);
				item.setCommentedUserId(commentedId);
				item.setCommentId(commentId);
				item.setCommentText(commentText);
				item.setPublishUserId(publishId);
				if(map.containsKey(tlInfoId)){
					List<TimeLineCommentInfoItem> list = map.get(tlInfoId);
					list.add(item);
				}else{
					List<TimeLineCommentInfoItem> list = new ArrayList<TimeLineCommentInfoItem>();
					list.add(item);
					map.put(tlInfoId, list);
				}
			}
			for(TimeLineInfoItem info : res){
				if(map.containsKey(info.getTlInfoId())){
					info.setCommentList(map.get(info.getTlInfoId()));
				}
			}
		}catch(Exception ex){
			log.error("wrapFindBuddyInfoListWithComment error", ex);
			throw ex;
		}
	}
	
	public static int insertClub(String ownerId,String addr,String name,String phone,String playStyle,double sale,double longitude,double latitude,String geohash,String clubImageUrl) throws Exception{
		try{
			String []params = {"@ownerid","@addr","@name","@phone","@playstyle","@sale","@longitude","@latitude","@geohash","@clubimageurl"};
			Object []values = {ownerId,addr,name,phone,playStyle,sale,longitude,latitude,geohash,clubImageUrl};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertClub", params, values);
			return dt.getRow(0).getInt(1);
		}catch(Exception ex){
			log.error("insertFindBuddyInfo error", ex);
			throw ex;
		}
	}
	
	public static void deleteClub(int clubId) throws Exception{
		try{
			Object []values = { clubId };
			happy8DB.executeNonQuery(sqlDeleteClub, values);
		}catch(Exception ex){
			log.error("deleteClub error", ex);
			throw ex;
		}
	}
	
	public static int updateClub(String sql,Object []values) throws Exception{
		try{
			int res = happy8DB.executeNonQuery(sql, values);
			if(res > 0)
				return 200;
			return 404;
		}catch(Exception ex){
			log.error("updateClub error", ex);
			throw ex;
		}
	}
	
	public static void insertFavoriteClub(String userId,int clubId) throws Exception{
		try{
			Object []values = {userId, clubId };
			happy8DB.executeNonQuery(sqlInsertFavoriteClub, values);
		}catch(Exception ex){
			log.error("insertFavoriteClub error", ex);
			throw ex;
		}
	}
	
	public static void delFavoriteClub(String userId,int clubId) throws Exception{
		try{
			Object []values = {userId, clubId };
			happy8DB.executeNonQuery(sqlDeleteFavoriteClub, values);
		}catch(Exception ex){
			log.error("delFavoriteClub error", ex);
			throw ex;
		}
	}
	
	public static List<ClubItem> getFavoriteClubList(String userId,int start,int end) throws Exception{
		try{
			List<ClubItem> res = new ArrayList<ClubItem>();
			int count = end - start ;
			Object []values = {userId,start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectFavoriteClubList, values);
			for(DataRow dr : dt.getRows()){
				ClubItem item = new ClubItem();
				item.setAddr(dr.getString("addr"));
				item.setName(dr.getString("name"));
				item.setClubId(dr.getInt("clubid"));
				item.setLatitude(dr.getDouble("latitude"));
				item.setLongitude(dr.getDouble("longitude"));
				item.setOwnerId(dr.getString("ownerid"));
				item.setPlayStyle(dr.getString("playstyle"));
				item.setStatus(dr.getInt("status"));
				item.setSale(dr.getDouble("sale"));
				item.setPhone(dr.getString("phone"));
				item.setClubImageUrl(dr.getString("clubimageurl"));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getTimeLineList error", ex);
			throw ex;
		}
	}
	
	public static List<ClubItem> getUnApproveClubList(int start,int end) throws Exception{
		try{
			List<ClubItem> res = new ArrayList<ClubItem>();
			int count = end - start ;
			Object []values = {start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectUnApproveClubList, values);
			for(DataRow dr : dt.getRows()){
				ClubItem item = new ClubItem();
				item.setAddr(dr.getString("addr"));
				item.setName(dr.getString("name"));
				item.setClubId(dr.getInt("clubid"));
				item.setLatitude(dr.getDouble("latitude"));
				item.setLongitude(dr.getDouble("longitude"));
				item.setOwnerId(dr.getString("ownerid"));
				item.setPlayStyle(dr.getString("playstyle"));
				item.setPhone(dr.getString("phone"));
				item.setSale(dr.getDouble("sale"));
				item.setClubImageUrl(dr.getString("clubimageurl"));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getTimeLineList error", ex);
			throw ex;
		}
	}
	
	public static List<ClubItem> getMyOwnClubList(String userId,int start,int end) throws Exception{
		try{
			List<ClubItem> res = new ArrayList<ClubItem>();
			int count = end - start ;
			Object []values = {userId,start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectMyOwnClubList, values);
			for(DataRow dr : dt.getRows()){
				ClubItem item = new ClubItem();
				item.setAddr(dr.getString("addr"));
				item.setClubId(dr.getInt("clubid"));
				item.setName(dr.getString("name"));
				item.setLatitude(dr.getDouble("latitude"));
				item.setLongitude(dr.getDouble("longitude"));
				item.setStatus(dr.getInt("status"));
				item.setOwnerId(dr.getString("ownerid"));
				item.setPlayStyle(dr.getString("playstyle"));
				item.setPhone(dr.getString("phone"));
				item.setSale(dr.getDouble("sale"));
				item.setClubImageUrl(dr.getString("clubimageurl"));
				item.setStatus(dr.getInt("status"));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getMyOwnClubList error", ex);
			throw ex;
		}
	}
	
	public static List<ClubItem> queryClubList(String index,String type,int start,int end) throws Exception{
		try{
			List<ClubItem> res = new ArrayList<ClubItem>();
			int count = end - start;
			String queryIndex = "%"+index+"%";
			Object []values = {queryIndex , start , count};
			String sql = "";
			if(type.equals("tel")){
				sql = sqlQueryClubListByTel;
			}else{
				sql = sqlQueryClubListByAddr;
			}
			DataTable dt = happy8DB.executeTable(sql, values);
			for(DataRow dr : dt.getRows()){
				ClubItem item = new ClubItem();
				item.setAddr(dr.getString("addr"));
				item.setName(dr.getString("name"));
				item.setClubId(dr.getInt("clubid"));
				item.setLatitude(dr.getDouble("latitude"));
				item.setStatus(dr.getInt("status"));
				item.setLongitude(dr.getDouble("longitude"));
				item.setOwnerId(dr.getString("ownerid"));
				item.setPhone(dr.getString("phone"));
				item.setPlayStyle(dr.getString("playstyle"));
				item.setSale(dr.getDouble("sale"));
				item.setClubImageUrl(dr.getString("clubimageurl"));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getTimeLineList error", ex);
			throw ex;
		}
	}

	public static ClubItem getClubItem(int clubId) throws Exception {
		Object[] values = { clubId };
		try {
			DataTable dt = happy8DB.executeTable(sqlSelectClubItem, values);
			if (dt.getRowCount() == 0)
				return null;
			DataRow dr = dt.getRow(0);
			ClubItem item = new ClubItem();
			item.setAddr(dr.getString("addr"));
			item.setName(dr.getString("name"));
			item.setClubId(dr.getInt("clubid"));
			item.setLatitude(dr.getDouble("latitude"));
			item.setStatus(dr.getInt("status"));
			item.setLongitude(dr.getDouble("longitude"));
			item.setOwnerId(dr.getString("ownerid"));
			item.setPhone(dr.getString("phone"));
			item.setPlayStyle(dr.getString("playstyle"));
			item.setSale(dr.getDouble("sale"));
			item.setClubImageUrl(dr.getString("clubimageurl"));
			return item;

		} catch (Exception ex) {
			log.error("getClubItem error", ex);
			throw ex;
		}
	}
	
	public static List<ClubItem> getNearByClubList(String[] adjAndgeo,int start,int end) throws Exception{
		try{
			List<ClubItem> res = new ArrayList<ClubItem>();
			int count = end - start ;
			//String queryIndex = geoHash+"%";
			Object []values = new Object[adjAndgeo.length + 2];
			for(int i=0;i<adjAndgeo.length;i++){
				values[i] = adjAndgeo[i]+"%";
			}
			values[adjAndgeo.length] = start;
			values[adjAndgeo.length+1] = count;
			DataTable dt = happy8DB.executeTable(sqlQueryClubListByGeoHash, values);
			for(DataRow dr : dt.getRows()){
				ClubItem item = new ClubItem();
				item.setAddr(dr.getString("addr"));
				item.setName(dr.getString("name"));
				item.setClubId(dr.getInt("clubid"));
				item.setStatus(dr.getInt("status"));
				item.setLatitude(dr.getDouble("latitude"));
				item.setLongitude(dr.getDouble("longitude"));
				item.setOwnerId(dr.getString("ownerid"));
				item.setPhone(dr.getString("phone"));
				item.setPlayStyle(dr.getString("playstyle"));
				item.setSale(dr.getDouble("sale"));
				item.setClubImageUrl(dr.getString("clubimageurl"));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getTimeLineList error", ex);
			throw ex;
		}
	}
	
	public static void insertAddFriendReq(String userId,String friendId) throws Exception{
		try{
			Object []values = {userId,friendId};
			happy8DB.executeNonQuery(sqlInsertAddFriendReq, values);
		}catch(Exception ex){
			log.error("insertAddFriendReq error", ex);
			throw ex;
		}
	}
	
	public static int delAddFriendReq(String userId,String friendId) throws Exception{
		try{
			Object []values = {userId,friendId};
			return happy8DB.executeNonQuery(sqlDeleteAddFriendReq, values);
		}catch(Exception ex){
			log.error("delAddFriendReq error", ex);
			throw ex;
		}
	}
	
	public static void insertFriend(String userId,String friendId) throws Exception{
		try{
			Object []values = {userId,friendId};
			happy8DB.executeNonQuery(sqlInsetFriendReq, values);
		}catch(Exception ex){
			log.error("insertFriend error", ex);
			throw ex;
		}
	}
	
	public static void deleteFriend(String userId,String friendId) throws Exception{
		try{
			Object []values = {userId,friendId};
			happy8DB.executeNonQuery(sqlDeleteFriend, values);
		}catch(Exception ex){
			log.error("delteFriend error", ex);
			throw ex;
		}
	}
	
	public static FindBuddyCommentInfo getFindBuddyReplay(long commitId)
			throws Exception {
		try {
			Object[] values = { commitId };
			DataTable dt = happy8DB.executeTable(sqlSelectFrindBuddyReplay,
					values);
			if (dt.getRowCount() == 0)
				return null;
			FindBuddyCommentInfo item = new FindBuddyCommentInfo();
			DataRow dr = dt.getRow(0);
			long bdInfoId = dr.getLong("bdinfoid");
			long commentId = dr.getLong("commentid");
			String publishId = dr.getString("publishid");
			String commentText = dr.getString("commenttext");

			item.setBdInfoId(bdInfoId);
			item.setCommentId(commentId);
			item.setCommentText(commentText);
			item.setPublishUserId(publishId);
			item.setAvatarUrl(dr.getString("avatarurl"));
			item.setSignature(dr.getString("signature"));
			item.setDateTime(dateFormat(dr.getDateTime("createdate")));

			return item;
		} catch (Exception ex) {
			log.error("getFindBuddyReplay error", ex);
			throw ex;
		}
	}
	
	public static long insertBookClub(String userId,long clubId,int tableIndex,int chairIndex,Date startTime,int duration) throws Exception{
		try{
			String []params = {"@userid","@clubid","@tableindex","@chairIndex","@startTime","@duration"};
			Object []values = {userId,clubId,tableIndex,chairIndex,startTime,duration};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertBookClub", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertBookClub error", ex);
			throw ex;
		}
	}
	
	public static long insertOrder(double amount) throws Exception{
		try{
			String []params = {"@paystatus","@amount"};
			Object []values = { 0 , amount};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertOrder", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertOrder error", ex);
			throw ex;
		}
	}
	
	public static void insertOrderDetail(long orderId,int tableId, Date date,int gameTime, String userId) throws Exception{
		try{
			Object []values = { orderId,userId,tableId,date,gameTime };
			happy8DB.executeNonQuery(sqlInsertOrderDetail, values);
		}catch(Exception ex){
			log.error("insertOrderDetail error", ex);
			throw ex;
		}
	}
	
	public static void deleteOrderById(long orderId) throws Exception{
		try{
			Object []values = { orderId };
			happy8DB.executeNonQuery(sqlDeleteOrderById, values);
			happy8DB.executeNonQuery(sqlDeleteOrderDetail, values);
		}catch(Exception ex){
			log.error("deleteOrderById error", ex);
			throw ex;
		}
	}
	
	public static List<OrderItem> getOrderList(String userId,Date date,int start,int end) throws Exception{
		try{
			List<OrderItem> res = new ArrayList<OrderItem>();
			int count = end - start;
			Object []values = {userId,date,start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectOrderList, values);
			for(DataRow dr : dt.getRows()){
				OrderItem item = new OrderItem();
				item.setUserId(dr.getString("userid"));
				item.setClubName(dr.getString("name"));
				item.setDate(StringUtils.Date2String(dr.getDateTime("date")));
				item.setClubId(dr.getInt("clubid"));
				item.setGameTime(dr.getInt("gametime"));
				item.setOrderId(dr.getLong("orderid"));
				item.setTableId(dr.getInt("tableid"));
				item.setTableName(dr.getString("tablename"));
				item.setStatus(dr.getInt("paystatus"));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getOrderList error", ex);
			throw ex;
		}
	}
	
	public static List<QueryTableItem> getQueryTableList(Date date,int gameTime,int clubId,int start,int end) throws Exception{
		try{
			List<QueryTableItem> res = new ArrayList<QueryTableItem>();
			int count = end - start;
			Object []values = {clubId,start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectTablesByClubId, values);
			for(DataRow dr : dt.getRows()){
				QueryTableItem item = new QueryTableItem();
				item.setTableId(dr.getInt("tableid"));
				item.setTableName(dr.getString("tablename"));
				item.setType(dr.getInt("type"));
				item.setPrice(dr.getFloat("price"));
				item.setUrl(dr.getString("url"));
				setQueryItemStatus(item, date, gameTime, item.getTableId());
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getQueryTableList error", ex);
			throw ex;
		}
	}
	
	private static void setQueryItemStatus(QueryTableItem item,Date date,int gameTime,int tableId) throws Exception{
		Object []values = { tableId,date,gameTime };
		try{
			DataTable dt = happy8DB.executeTable(sqlSelectOrderStatus, values);
			if(dt.getRowCount() == 0)
				return;
			
			String userId = dt.getRow(0).getString("userid");
			Date createDate = dt.getRow(0).getDateTime("createdate");
			String signature = dt.getRow(0).getString("signature");
			int payStatus = dt.getRow(0).getInt("paystatus");
			
			if(payStatus == 1){
				item.setOrderStatus(1);
				item.setUserId(userId);
				item.setSignature(signature);
			}
			
			Date now = new Date();
			if(now.getTime() - createDate.getTime() < 15 * 60 * 1000){
				item.setOrderStatus(1);
				item.setUserId(userId);
				item.setSignature(signature);
			}
		}catch(Exception ex){
			log.error("setQueryItemStatus error", ex);
			throw ex;
		}
	}
	
	public static boolean isUserSuperAdmin(String userId) throws Exception{
		try{
			Object []values = { userId };
			DataTable dt = happy8DB.executeTable(sqlIsUserSuperAdimin, values);
			if(dt.getRowCount() > 0)
				return true;
			
			return false;
		}catch(Exception ex){
			log.error("isUserSuperAdmin error", ex);
			throw ex;
		}
	}
	
	public static boolean insertUserCoupon(String userId,String couponId) throws Exception{
		try{
			Object []values = { userId,couponId ,new Date(),new Date()};
			int rowCount = happy8DB.executeNonQuery(sqlInsertUserCoupon, values);
			if(rowCount == 1){
				return true;
			}
			return false;
		}catch(Exception ex){
			log.error("insertUserCoupon error", ex);
			throw ex;
		}
	}
	
	public static void insertCoupon(String couponId,int type,int disCount,double value,double startAmout,Date expireTime) throws Exception{
		try{
			Object []values = { couponId,type,disCount,value,startAmout,expireTime };
			happy8DB.executeNonQuery(sqlInsertCoupon, values);
		}catch(Exception ex){
			log.error("insertCoupon error", ex);
			throw ex;
		}
	}
	
	public static void deleteCoupon(String couponId) throws Exception{
		try{
			Object []values = { couponId };
			happy8DB.executeNonQuery(sqlDeleteCoupon, values);
		}catch(Exception ex){
			log.error("deleteCoupon error", ex);
			throw ex;
		}
	}
	
	public static List<CouponItem> getAllMyCouponList(String userId,int start,int end) throws Exception{
		try{
			List<CouponItem> res = new ArrayList<CouponItem>();
			int count = end - start;
			Object []values = {userId,start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectMyCouponList, values);
			for(DataRow dr : dt.getRows()){
				CouponItem item = new CouponItem();
				item.setCouponId(dr.getString("couponid"));
				item.setDisCount(dr.getInt("discount"));
				item.setStartAmout(dr.getDouble("startamout"));
				item.setType(dr.getInt("type"));
				item.setValue(dr.getDouble("value"));
				int status = StringUtils.isNullOrEmpty(dr.getString("consumecouponid")) ? 1 : 2;
				item.setStatus(status);
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getAllMyCouponList error", ex);
			throw ex;
		}
	}
	
	public static List<CouponItem> getUnConsumeCouponList(int start,int end) throws Exception{
		try{
			List<CouponItem> res = new ArrayList<CouponItem>();
			int count = end - start;
			Object []values = {new Date(),start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectUnConsumeCouponList, values);
			for(DataRow dr : dt.getRows()){
				CouponItem item = new CouponItem();
				item.setCouponId(dr.getString("couponid"));
				item.setDisCount(dr.getInt("discount"));
				item.setStartAmout(dr.getDouble("startamout"));
				item.setType(dr.getInt("type"));
				item.setValue(dr.getDouble("value"));
				item.setStatus(1);
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getUnConsumeCouponList error", ex);
			throw ex;
		}
	}
	
	public static List<CouponItem> getConsumeCouponList(String userId,int start,int end) throws Exception{
		try{
			List<CouponItem> res = new ArrayList<CouponItem>();
			int count = end - start;
			Object []values = {userId,start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectConsumeCouponList, values);
			for(DataRow dr : dt.getRows()){
				CouponItem item = new CouponItem();
				item.setCouponId(dr.getString("couponid"));
				item.setDisCount(dr.getInt("discount"));
				item.setStartAmout(dr.getDouble("startamout"));
				item.setType(dr.getInt("type"));
				item.setValue(dr.getDouble("value"));
				item.setStatus(2);
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getConsumeCouponList error", ex);
			throw ex;
		}
	}
	
	public static void updateClubStatus(int clubId,int status) throws Exception{
		try{
			Object []values = { status, clubId};
			happy8DB.executeNonQuery(sqlUpdateClubStatus, values);
		}catch(Exception ex){
			log.error("updateClubStatus error", ex);
			throw ex;
		}
	}
	
	public static void deleteFindBuddyInfo(long fdInfoId) throws Exception{
		try{
			Object []values = { fdInfoId };
			happy8DB.executeNonQuery(sqlDeleteFindBuddyInfo, values);
			happy8DB.executeNonQuery(sqlDeleteFindBuddyReplayAllOne, values);
		}catch(Exception ex){
			log.error("deleteFindBuddyInfo error", ex);
			throw ex;
		}
	}
	
	public static void deleteFindBuddyReplay(long fdReplayId) throws Exception{
		try{
			Object []values = { fdReplayId };
			happy8DB.executeNonQuery(sqlDeleteFindBuddyReplay, values);
		}catch(Exception ex){
			log.error("deleteFindBuddyReplay error", ex);
			throw ex;
		}
	}
	
	public static int rateClub(int clubId,String userId,int rate) throws Exception{
		try{
			Object []values = { clubId,userId,rate };
			int row = happy8DB.executeNonQuery(sqlInsertUserRate, values);
			if(row == 2)
				return 405;
			
			Object []values1 = { clubId};
			DataTable dt = happy8DB.executeTable(sqlSelectSumRateAndCount, values1);
			if(dt.getRowCount() == 0){
				return 200;
			}
			int sum = dt.getRow(0).getInt("sum");
			int count = dt.getRow(0).getInt("count");
			float value = (float)sum / (float) count;
			BigDecimal bd = new BigDecimal((double)value);
			int   scale  =   1;//设置位数    
			int   roundingMode  =  4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.    
			bd.setScale(scale,roundingMode);
			
			Object []values2 = { clubId,bd.floatValue()};
			happy8DB.executeNonQuery(sqlUpdateClubRate, values2);
			
			return 200;
		}catch(Exception ex){
			log.error("rateClub error", ex);
			throw ex;
		}
	}
	
	public static UserLevelArgs getUserLevel(String userId) throws Exception{
		try{
			UserLevelArgs args = new UserLevelArgs();
			Object []values = { userId };
			DataTable dt = happy8DB.executeTable(sqlSelectUserLevel, values);
			if(dt.getRowCount() == 0){
				return null;
			}
			args.setUserLevel(dt.getRow(0).getInt("usertype"));
			args.setUserStatus(dt.getRow(0).getInt("userstatus"));
			return args;
		}catch(Exception ex){
			log.error("getUserLevel error", ex);
			throw ex;
		}
	}
	
	public static CheckOrderItem getCheckOrder(int tableId,Date date,int gameTime) throws Exception{
		try{
			CheckOrderItem args = new CheckOrderItem();
			Object []values = { tableId,date,gameTime };
			DataTable dt = happy8DB.executeTable(sqlSelectOrderByDate, values);
			if(dt.getRowCount() == 0){
				return null;
			}
			args.setOrderId(dt.getRow(0).getInt("orderid"));
			args.setStatus(dt.getRow(0).getInt("paystatus"));
			args.setCreateDate(dt.getRow(0).getDateTime("createdate"));
			return args;
		}catch(Exception ex){
			log.error("getCheckOrder error", ex);
			throw ex;
		}
	}
	
	public static void deleteNoPayOrder(int orderId) throws Exception{

		Object []values = { orderId };
		try{
			happy8DB.executeNonQuery(sqlDeleteNoPayOrder, values);
			happy8DB.executeNonQuery(sqlDeleteOrderDetail, values);
		}catch(Exception ex){
			log.error("deleteNoPayOrder error", ex);
			throw ex;
		}
	}
	
	public static long insertOrder(int tableId,Date date,int gameTime,String userId) throws Exception{
		try{
			String []params = {"@userid","@tableid","date","gametime"};
			Object []values = {userId,tableId,date,gameTime};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertOrder", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertOrder error", ex);
			throw ex;
		}
	}
	
	public static List<SystemNotifyItem> getSystemNotify(int start,int end) throws Exception{
		try{
			List<SystemNotifyItem> res = new ArrayList<SystemNotifyItem>();
			int count = end - start ;
			Object []values = {start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectSystemNotify, values);
			for(DataRow dr : dt.getRows()){
				SystemNotifyItem item = new SystemNotifyItem();
				item.setTitle(dr.getString("title"));
				item.setSnId(dr.getInt("id"));
				item.setContent(dr.getString("content"));
				item.setSendTime(StringUtils.Date2String(dr.getDateTime("sendtime")));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getSystemNotify error", ex);
			throw ex;
		}
	}
	
	public static List<SystemNotifyItem> getUnSendSystemNotify() throws Exception{
		try{
			Object []values = {new Date()};
			List<SystemNotifyItem> res = new ArrayList<SystemNotifyItem>();
			DataTable dt = happy8DB.executeTable(sqlSelectUnSendNotify, values);
			for(DataRow dr : dt.getRows()){
				SystemNotifyItem item = new SystemNotifyItem();
				item.setTitle(dr.getString("title"));
				item.setSnId(dr.getInt("id"));
				item.setContent(dr.getString("content"));
				item.setSendTime(StringUtils.Date2String(dr.getDateTime("sendtime")));
				res.add(item);
			}
			return res;
		}catch(Exception ex){
			log.error("getUnSendSystemNotify error", ex);
			throw ex;
		}
	}
	
	private static String sqlUpdateSendFlag = "update ha_systemnotify set sendflag = 1 where id = ?";
	public static void updateSendFlag(int snId) throws Exception{
		try{
			Object []values = { snId };
			happy8DB.executeNonQuery(sqlUpdateSendFlag, values);
		}catch(Exception ex){
			log.error("updateSendFlag error", ex);
			throw ex;
		}
	}
	
	public static void updateUserStatus(String userId,int userStatus,String pushtoken) throws Exception{
		try{
			Object []values = { userStatus,pushtoken,userId };
			happy8DB.executeNonQuery(sqlUpdateUserStatus, values);
		}catch(Exception ex){
			log.error("updateUserStatus error", ex);
			throw ex;
		}
	}
	
	private static String dateFormat(Date date){
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format1.format(date);
	}
}
