package com.happy8.utils;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.happy8.args.UpdateOrderStatusItem;

public class TokenUtils {
	private static Logger log = LoggerFactory.getLogger(TokenUtils.class);
	public static UpdateOrderStatusItem parseToken(String token){
		try{
			String hexStr =  new String(StringUtils.base64Decode(token),"utf-8");
			String realStr = new String(StringUtils.parseHexStr2Byte(hexStr),"utf-8");
			String []items = realStr.split(",");
			UpdateOrderStatusItem item = new UpdateOrderStatusItem();
			item.setUserId(items[0]);
			item.setOrderId(Long.parseLong(items[1]));
			item.setStatus(Integer.parseInt(items[2]));
			return item;
		}catch(Exception ex){
			log.error("parse token error" , ex);
			return null;
		}
	}
	
	public static String encodeToken(String userId,long orderId,int status) throws UnsupportedEncodingException{
		String str = String.format("%s,%s,%s", userId,orderId,status);
		String hexStr = StringUtils.parseByte2HexStr(str.getBytes("utf-8"));
		return StringUtils.base64Encode(hexStr.getBytes("utf-8"),false);
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String token = encodeToken("13500000000", 23, 2);
		System.out.println(token);
		UpdateOrderStatusItem item = parseToken(token);
		System.out.println(item.getUserId() + item.getOrderId() + item.getStatus());
	}
}
