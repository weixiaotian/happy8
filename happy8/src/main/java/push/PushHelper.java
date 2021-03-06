package push;

import push.android.AndroidBroadcast;
import push.android.AndroidUnicast;
import push.ios.IOSBroadcast;
import push.ios.IOSUnicast;

public class PushHelper {
	private static String androidAppkey = "564fcc6f67e58e2bd0007462";
	private static String androidAppMasterSecret = "7qtelnppzwgfplcjjvft23c1ymbyyxur";
	private static String iosAppkey ="564fccc167e58e1485003cf7";
	private static String iosAppMasterSecret = "nhhhydsjgucxw1izbxsosn3aj1l1gzjt";
	
	private static PushClient client = new PushClient();
	
	public static boolean sendAndroidBroadcast(String ticker,String title,String content) throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast(androidAppkey,androidAppMasterSecret);
		broadcast.setTicker( ticker);
		broadcast.setTitle(  title );
		broadcast.setText( content );
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		broadcast.setProductionMode();
		// Set customized fields
		//broadcast.setExtraField("test", "helloworld");
		return client.send(broadcast);
	}
	
	public static void sendAndroidUnicast(String pushToken,String ticker,String title,String content) throws Exception {
		AndroidUnicast unicast = new AndroidUnicast(androidAppkey,androidAppMasterSecret);
		// TODO Set your device token
		unicast.setDeviceToken( pushToken);
		unicast.setTicker( ticker);
		unicast.setTitle(  title);
		unicast.setText(   content);
		unicast.goAppAfterOpen();
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		unicast.setProductionMode();
		// Set customized fields
		//unicast.setExtraField("test", "helloworld");
		client.send(unicast);
	}
	
	
	public static boolean sendIOSBroadcast(String title) throws Exception {
		IOSBroadcast broadcast = new IOSBroadcast(iosAppkey,iosAppMasterSecret);

		broadcast.setAlert(title);
		broadcast.setBadge( 0);
		broadcast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		broadcast.setTestMode();
		// Set customized fields
		//broadcast.setCustomizedField("test", "helloworld");
		return client.send(broadcast);
	}
	
	public static void sendIOSUnicast(String pushToken,String title) throws Exception {
		IOSUnicast unicast = new IOSUnicast(iosAppkey,iosAppMasterSecret);
		// TODO Set your device token
		unicast.setDeviceToken( pushToken );
		unicast.setAlert(title);
		unicast.setBadge( 0);
		unicast.setSound( "default");
		// TODO set 'production_mode' to 'true' if your app is under production mode
		unicast.setTestMode();
		// Set customized fields
		//unicast.setCustomizedField("test", "helloworld");
		client.send(unicast);
	}
}
