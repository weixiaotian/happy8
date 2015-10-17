package com.happy8.dao;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tian.database.DataTable;
import tian.database.Database;
import tian.database.DatabaseManager;

import com.happy8.args.UserInfoArgs;

public class Happy8DAO {
	private static Logger log = LoggerFactory.getLogger(Happy8DAO.class);
	private static Database happy8DB = null;
	
	private static String sqlSelectUserInfo = "select userid,gender,signature,brief,avatarurl from ha_user where userid = ?";
	private static String sqlUserLogin = "select userid,password from ha_user where userid = ?";
	private static String sqlRegisterUser = "insert into ha_user(userid,password) values(?,?)";
	private static String sqlResetPassword = "update ha_user set password = ? where userid = ?";
	
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
}
