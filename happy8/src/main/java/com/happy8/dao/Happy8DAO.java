package com.happy8.dao;

import java.io.FileInputStream;
import java.util.ArrayList;
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

import com.happy8.args.FindBuddyCommentInfo;
import com.happy8.args.FindBuddyInfoItem;
import com.happy8.args.TimeLineCommentInfoItem;
import com.happy8.args.TimeLineInfoItem;
import com.happy8.args.UserInfoArgs;

public class Happy8DAO {
	private static Logger log = LoggerFactory.getLogger(Happy8DAO.class);
	private static Database happy8DB = null;
	
	private static String sqlSelectUserInfo = "select userid,gender,signature,brief,avatarurl from ha_user where userid = ?";
	private static String sqlUserLogin = "select userid,password from ha_user where userid = ?";
	private static String sqlRegisterUser = "insert into ha_user(userid,password) values(?,?)";
	private static String sqlResetPassword = "update ha_user set password = ? where userid = ?";
	private static String sqlSelectFindBuddyInfoList = "select bdinfoid,userid,infocontent from ha_findbuddyinfo order by bdinfoid desc limit ?,?"; 
	private static String sqlSelectFindBuddyCommentList = "SELECT commentid,bdinfoid,publishid,commentedid,commenttext FROM ha_findbuddycomment WHERE bdinfoid IN ( ? )";
	private static String sqlSelectTimeLineCommentList = "SELECT commentid,tlinfoid,publishid,commentedid,commenttext FROM ha_timelinecomment WHERE bdinfoid IN ( ? )";
	private static String sqlSelectFriendList = "select friendid from ha_friend where userid = ?";
	private static String sqlInsertUserTimeLine = "insert into ha_usertimeline values(?,?)";
	private static String sqlDeleteTimeLine = "delete from ha_usertimeline where tlinfoid = ?";
	private static String sqlDeleteUserTimeLine = "delete from ha_usertimeline where userid = ? and tlinfoid = ?";
	private static String sqlSelectTimeLineSelfSendList = "select tlinfoid,userid,infocontent from ha_timeline where userid = ? order by tlinfoid desc limit ?,?";
	private static String sqlSelectTimeLineList = "select b.tlinfoid,b.userid,b.infocontent from ha_usertimeline a,ha_timeline b where a.userid = ? and a.tlinfoid = b.tlinfoid order by a.tlinfoid desc limit ?,?";
	private static String sqlDeleteClub = "delete from ha_club where clubid = ?";
	
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
	
	public static long insertFindBuddyInfo(String userId,String infoContent) throws Exception{
		try{
			String []params = {"userid","infocontent"};
			Object []values = {userId,infoContent};
			DataTable dt = happy8DB.spExecuteTable("USP_InsertFindBuddyInfo", params, values);
			return dt.getRow(0).getLong(1);
		}catch(Exception ex){
			log.error("insertFindBuddyInfo error", ex);
			throw ex;
		}
	}
	
	public static long insertFindBuddyComment(long bdInfoId,String publishId,String commentedId,String commentText) throws Exception{
		try{
			String []params = {"bdinfoid", "publishid","commentedid","commenttext"};
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
	
	private static List<FindBuddyInfoItem> getFindBuddyInfoListNoComment(int start,int end) throws Exception{
		try{
			List<FindBuddyInfoItem> res = new ArrayList<FindBuddyInfoItem>();
			int count = end - start + 1;
			Object []values = {start,count};
			DataTable dt = happy8DB.executeTable(sqlSelectFindBuddyInfoList, values);
			for(DataRow dr : dt.getRows()){
				FindBuddyInfoItem item = new FindBuddyInfoItem();
				item.setBdInfoId(dr.getLong("bdinfoid"));
				item.setUserId(dr.getString("userid"));
				item.setInfoContent(dr.getString("infocontent"));
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
			DataTable dt = happy8DB.executeTable(sqlSelectFindBuddyCommentList, infoIds.toArray());
			Map<Long,List<FindBuddyCommentInfo>> map = new HashMap<Long,List<FindBuddyCommentInfo>>();
			for(DataRow dr : dt.getRows()){
				long bdInfoId = dr.getLong("bdinfoid");
				long commentId = dr.getLong("commentid");
				String publishId = dr.getString("publishid");
				String commentedId = dr.getString("commentedid");
				String commentText = dr.getString("commenttext");
				
				FindBuddyCommentInfo item = new FindBuddyCommentInfo();
				item.setBdInfoId(bdInfoId);
				item.setCommentedUserId(commentedId);
				item.setCommentId(commentId);
				item.setCommentText(commentText);
				item.setPublishUserId(publishId);
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
			String []params = {"userid","infocontent"};
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
			String []params = {"tlinfoid", "publishid","commentedid","commenttext"};
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
			int count = end - start + 1;
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
			int count = end - start + 1;
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
	
	private static void wrapTimeLineInfoListWithComment(List<TimeLineInfoItem> res) throws Exception{
		List<Long> infoIds =new ArrayList<Long>();
		for(TimeLineInfoItem info : res){
			infoIds.add(info.getTlInfoId());
		}
		
		try{
			DataTable dt = happy8DB.executeTable(sqlSelectTimeLineCommentList, infoIds.toArray());
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
	
	public static int insertClub(String ownerId,String addr,String phone,String playStyle,double sale,double longitude,double latitude,String geohash) throws Exception{
		try{
			String []params = {"ownerid","addr","phone","playstyle","sale","longitude","latitude","geohash"};
			Object []values = {ownerId,addr,phone,playStyle,sale,longitude,latitude,geohash};
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
			log.error("deleteTimeLine error", ex);
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
			log.error("updateUserInfo error", ex);
			throw ex;
		}
	}
}
