package com.happy8.weixinpay;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.happy8.args.OrderTableReqArgs;
import com.happy8.utils.StringUtils;
import com.tencent.common.Configure;
import com.tencent.common.HttpsRequest;
import com.tencent.common.Signature;
import com.tencent.common.XMLParser;

public class PayUtils {
	/**
	 * 创建统一下单的xml的java对象
	 * @param bizOrder 系统中的业务单号
	 * @param ip 用户的ip地址
	 * @param openId 用户的openId
	 * @return
	 * @throws Exception 
	 */
	 public static PayInfo createPayInfo(OrderTableReqArgs order,long orderId,String ip) throws Exception {
		  PayInfo payInfo = new PayInfo();
		  payInfo.setAppid(Configure.getAppid());
		  payInfo.setDevice_info("WEB");
		  payInfo.setMch_id(Configure.getMchid());
		  payInfo.setNonce_str(create_nonce_str().replace("-", ""));
		  payInfo.setBody("happy8 订单 " + orderId);
		  payInfo.setAttach(String.valueOf(orderId));
		  payInfo.setOut_trade_no(String.valueOf(orderId));
		  payInfo.setTotal_fee((int)(order.getAmount() * 100));//元转分
		  payInfo.setSpbill_create_ip(ip);
		  payInfo.setNotify_url("http://www.weixin.qq.com/wxpay/pay.php");
		  payInfo.setTrade_type("APP");
		  payInfo.setSign(getSign(payInfo));
		  //payInfo.setOpenid(openId);
		  return payInfo;
	 }
	 
	 public static Map<String,Object> postOrder(PayInfo payInfo) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, ParserConfigurationException, SAXException{
		 HttpsRequest req = new HttpsRequest();
		 String resStr = req.sendPost(Configure.UNIFIED_ORDER, payInfo);
		 return XMLParser.getMapFromXML(resStr);
	 }
	 
	 /**
	  * 获取签名
	  * @param payInfo
	  * @return
	  * @throws Exception
	  */
	  private static String getSign(PayInfo payInfo) throws Exception {
		   String signTemp = "appid="+payInfo.getAppid()
		    +"&attach="+payInfo.getAttach()
		    +"&body="+payInfo.getBody()
		    +"&device_info="+payInfo.getDevice_info()
		    +"&mch_id="+payInfo.getMch_id()
		    +"&nonce_str="+payInfo.getNonce_str()
		    +"&notify_url="+payInfo.getNotify_url()
		    +"&openid="+payInfo.getOpenid()
		    +"&out_trade_no="+payInfo.getOut_trade_no()
		    +"&spbill_create_ip="+payInfo.getSpbill_create_ip()
		    +"&total_fee="+payInfo.getTotal_fee()
		    +"&trade_type="+payInfo.getTrade_type()
		    +"&key="+Configure.getKey(); //这个key注意
		  MessageDigest md = MessageDigest.getInstance("MD5");
		  md.reset();
		  md.update(signTemp.getBytes("UTF-8"));
		  String sign = StringUtils.parseByte2HexStr(md.digest()).toUpperCase();
		  return sign;
	  }
	  
	  
	  /**
	  * 获取ip地址
	  * @param request
	  * @return
	 * @throws UnknownHostException 
	  */
	  private static String getIpAddr() throws UnknownHostException { 
		   InetAddress addr = null; 
		   addr = InetAddress.getLocalHost(); 
		   byte[] ipAddr = addr.getAddress(); 
		   String ipAddrStr = ""; 
		   for (int i = 0; i < ipAddr.length; i++) { 
		   if (i > 0) { 
		    ipAddrStr += "."; 
		   } 
		   ipAddrStr += ipAddr[i] & 0xFF; 
		   } 
		   return ipAddrStr; 
	  }
	  
	  private static String create_nonce_str() {
		  return UUID.randomUUID().toString().replace("-","");
	  }
	  
	  public static void main(String[] args) {
		  try{
			  OrderTableReqArgs order = new OrderTableReqArgs();
			  order.setAmount(100);
			  PayInfo payInfo = createPayInfo(order, 124, getIpAddr());
			  Map<String,Object> res = postOrder(payInfo);
			  String code = String.valueOf(res.get("return_code"));
			  String err_code = String.valueOf(res.get("err_code"));
			  String trade_type = String.valueOf(res.get("trade_type"));
			  String prepay_id = String.valueOf(res.get("prepay_id"));
			  String code_url = String.valueOf(res.get("code_url"));
			  System.out.println(code + trade_type + prepay_id + code_url + err_code);
		  }catch(Exception ex){
			  ex.printStackTrace();
		  }
	}
}
