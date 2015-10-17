package com.happy8.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInvokeHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpInvokeHelper.class);

	/**
	 * http post 请求通用接口
	 * 
	 * @param urlStr
	 * @param fileData
	 * @return
	 * @throws Exception
	 */
	public static String invokPost(String urlStr, String body) throws Exception {

		if (null == urlStr || urlStr.trim().equals("")) {
			throw new NullPointerException("invokPost url is null ");
		}

		LOGGER.info(String.format("invok post url =%s,xmlContent = %s", urlStr, body));

		HttpURLConnection con = null;
		OutputStream out = null;
		try {
			URL url = new URL(urlStr);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			/**
			 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
			 */
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(5000);
			con.setRequestProperty("Accept-Charset", "utf-8");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "application/json");
			out=con.getOutputStream();   
			out.write(body.getBytes("utf-8"));
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					out = null;
				}
			}
		}
		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = null;
		InputStreamReader input = new InputStreamReader(con.getInputStream(), "UTF-8");
		try {
			br = new BufferedReader(input);
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
			return buffer.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					br = null;
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					input = null;
				}
			}
		}

	}
	
	/**
	 * http get 请求通用接口
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static String invokGet(String urlStr) throws Exception {

		if (null == urlStr || urlStr.trim().equals("")) {
			throw new NullPointerException("invokGet url is null ");
		}

		LOGGER.info(String.format("invok get url =%s", urlStr));

		HttpURLConnection con = null;
		OutputStreamWriter out = null;
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(5000);
			con.setRequestProperty("Accept-Charset", "utf-8");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.connect();
			  // 取得输入流，并使用Reader读取
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                con.getInputStream()));
	        String lines;
	        
	        while ((lines = reader.readLine()) != null){
	            System.out.println(lines);
	            sb.append(lines);
	        }

	        reader.close();
	        // 断开连接	        
	        return sb.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			con.disconnect();
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					out = null;
				}
			}
		}
	}

}
