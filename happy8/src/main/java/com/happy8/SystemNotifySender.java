package com.happy8;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import push.PushHelper;

import com.happy8.args.SystemNotifyItem;
import com.happy8.dao.Happy8DAO;


public class SystemNotifySender {
	private static Logger log = LoggerFactory.getLogger(SystemNotifySender.class);
	public static void start(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					try{
						List<SystemNotifyItem> res = Happy8DAO.getUnSendSystemNotify();
						for(SystemNotifyItem item : res){
							
							PushHelper.sendIOSBroadcast(item.getTitle());
							PushHelper.sendAndroidBroadcast(item.getTitle(), item.getTitle(), item.getTitle());
							
							Happy8DAO.updateSendFlag(item.getSnId());
						}
						Thread.sleep(60 * 1000);
					}catch(Exception ex){
						log.error("process error", ex);
						try {
							Thread.sleep(5 * 60 * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		thread.setDaemon(false);
		thread.start();
	}
}
