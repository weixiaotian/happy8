package com.happy8.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTools {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpTools.class);
	
	public static String httpReqToStr(HttpServletRequest request){
		StringBuffer sb=new StringBuffer();
		String NEWLINE = "\r\n";
		sb.append("\r\nHEADER : ");
		Enumeration<String> hn= request.getHeaderNames();
		while(hn.hasMoreElements()){
			String tmp=hn.nextElement();
			sb.append(tmp);
			sb.append("=");
			sb.append(request.getHeader(tmp));
			sb.append(NEWLINE);
		}
		sb.append("URI : ");
		sb.append(request.getRequestURI());
		sb.append("?");
		sb.append(request.getQueryString());
		return sb.toString();
	}
		
	public static String[] checkAndGetRanges(String range){
		boolean issid = false;
        Pattern regex = Pattern.compile("^[0-9]+-[0-9]+$");
        issid = regex.matcher(range).matches();
        if(!issid)
        	return null;
        String[] rangeA=range.split("-");
        if(Integer.valueOf(rangeA[1])<=Integer.valueOf(rangeA[0]))
        	return null;
        return rangeA;
	}
	
	
	public static String formatDate(Date date){
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sFormat.format(date);
	}

	
	public static String getPath(String[] arrays){
		StringBuffer sb=new StringBuffer();
		sb.append(arrays[0]);
		sb.append("/");
		sb.append(arrays[1]);
		return sb.toString();
	}
	
	
	
	public static long getOperationTime(){
		return System.currentTimeMillis();
	}
	
	public static String getRequestBody(HttpServletRequest req){
		try{
			InputStream input = req.getInputStream();
			int contentLength = req.getContentLength();
			byte []bodyBytes = new byte[contentLength];
			input.read(bodyBytes);
			
			return new String(bodyBytes,"utf-8");
		}catch(Exception ex){
			LOGGER.error("getRequestEntity error",ex);
			return null;
		}
	}
	
	public static void sendOkResponse(HttpServletResponse response,String body) throws IOException{
		byte []bodyBytes = body.getBytes("utf-8");
		response.setStatus(200);
		response.setContentType("application/json");
		response.setContentLength(bodyBytes.length);
		response.getOutputStream().write(bodyBytes);
	}
	
	public static void sendResponseOnlyStatusCode(HttpServletResponse response,int statusCode){
		response.setStatus(statusCode);
	}
}
