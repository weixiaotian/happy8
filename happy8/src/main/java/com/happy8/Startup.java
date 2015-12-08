package com.happy8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Startup {
	private static Logger log = LoggerFactory.getLogger(Startup.class);
	public static void main(String[] args) {
		
		Happy8HttpService service = new Happy8HttpService();
		try {
			service.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("start service error!", e);
		}
		log.info("service start ok!");
	}
}
