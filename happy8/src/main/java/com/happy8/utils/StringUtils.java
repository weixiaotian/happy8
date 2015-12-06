package com.happy8.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import sun.misc.BASE64Encoder;

import com.mysql.jdbc.Field;

public class StringUtils {
	private static final int SIZE = 10;
	private static final ArrayBlockingQueue<BASE64Encoder> base64Encoders = new ArrayBlockingQueue<BASE64Encoder>(SIZE);
	
	static {
		for(int index = 0;index < SIZE;index++) {
			base64Encoders.add(new BASE64Encoder());
		}
	}
	
	public static boolean isNullOrEmpty(String str){
		if(str == null || str.equals("")){
			return true;
		}
		return false;
	}
	
	public static Date parse2Date(String str) throws ParseException{
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.parse(str);
	}
	
	public static String Date2String(Date date){
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static boolean isJsonFieldString(String json,String filed){
		int index = json.lastIndexOf(filed);
		if(index < 0){
			return false;
		}
		if(json.charAt(index+filed.length()+2) == '"'){
			return true;
		}
		return false;
	}
	
	/**
	 * 将二进制转换成16进制
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	/**
	 * Decode the input byte array to base64 String
	 * Use a object pool to guarantee the thread safe
	 * 
	 * 
	 * 每隔76个字符加一个换行符，在结尾再加一个换行符
	 * 换行符根据操作系统不同而变化。
	 * 
	 * @param buffer
	 * @return encoded base64 string; 
	 */
	public static String base64Encode(byte[] buffer) {
		return base64Encode(buffer,true,true);
	}

	/**
	 * Decode the input byte array to base64 String
	 * Use a object pool to guarantee the thread safe
	 * 
	 * 在结尾不加换行符
	 * 换行符采用\r\n
	 * 
	 * @param buffer
	 * @param lineSep a flag indicates whether insert a line separator after 76 chars
	 * @return original String object
	 */
	public static String base64Encode(byte[] buffer,boolean lineSep) {
		return base64Encode(buffer,lineSep,false);
	}
	/**
	 * Decode the input byte array to base64 String
	 * Use a object pool to guarantee the thread safe
	 * 当lineSep和endWithLineSep为true时，换行符随着操作系统而变化；在其他情况下，换行符为\r\n
	 * @param buffer
	 * @param lineSep a flag indicates whether insert a line separator after 76 chars, if lineSep is false, then endWithLineSep will be looked as false.
	 * @param endWithLineSep a flag indicates whether insert a line separator at the end.
	 * @return original String object
	 */
	public static String base64Encode(byte[] buffer,boolean lineSep,boolean endWithLineSep) {
		if(!lineSep) {
			endWithLineSep = false;
		}
		
		if(lineSep == true) {
			if(endWithLineSep) {
				//每76个字符加换行符，最后已换行符结尾。
				try {
					BASE64Encoder encoder = base64Encoders.poll(30, TimeUnit.SECONDS);
					if(encoder == null) {
						throw new RuntimeException("No available Base64Encoder!");
					}
					try {
						return encoder.encodeBuffer(buffer);
					} finally {
						base64Encoders.add(encoder);
					}
				} catch(RuntimeException ex) {
					throw ex;
				} catch(Exception ex) {
					throw new RuntimeException(ex);
				}
			} else {
				//每76个字符加换行符，不以换行符结尾。
				return Base64.encodeToString(buffer, lineSep);
			}
		} else {
			//不加换行符
			return Base64.encodeToString(buffer, false);
		}
	}

	/**
	 * Encoding input String to base64 encoded String
	 * thread safe
	 * 
	 * illegal chars at the begin and end will be trimmed.
	 * @param str
	 * @return original byte[] object
	 * @throws IOException
	 */
	public static byte[] base64Decode(String str) throws IOException {
		return Base64.decode(str);
	}
	
//	public static void main(String[] args) {
//		System.out.println(isJsonFieldString("{\"detail\":\"", "detail"));
//	}
}
