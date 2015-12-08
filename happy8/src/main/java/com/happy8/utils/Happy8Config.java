package com.happy8.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Happy8Config {
	public static int Port = 8080;
	
	public static void init() throws FileNotFoundException, IOException{
		Properties p = new Properties();
		p.load(new FileInputStream("happy8.properties"));
		Port = Integer.parseInt( p.getProperty("serverport"));
	}
	
}
