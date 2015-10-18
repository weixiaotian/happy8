package com.happy8.utils;
public class GeoHashTool {

	final static char[] BASECODE = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
									 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 
									 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	/**
	 * 对经纬度使用GeoHash算法，生成 GeoHash编码
	 * 
	 * @param longt
	 *            经度（角度，精确到小数点后六位）
	 * @param lat
	 *            纬度（角度，精确到小数点后六位）
	 * 
	 * @return 生成的GeoHash编码
	 */
	public static String generateGeoHashCode(double longt, double lat) {

		long longtL = (long)(longt* 1000000);
		long latL = (long)(lat* 1000000);
		
		return getGeoHashCode(getLongtCodeString(longtL), getLatCodeString(latL));
	}

	/**
	 * 合并经纬度二进制编码串，生成GeoHash编码
	 * 
	 * @param longtCode
	 *            二进制经度编码串
	 * @param latCode
	 *            二进制纬度编码串
	 * @return GeoHash 编码
	 */
	private static String getGeoHashCode(char[] longtCode, char[] latCode) {

		// 合并经纬度编码，偶数位是经度，奇数位是纬度
		char[] encode = new char[50];
		for (int i = 0; i < 50; i++) {
			encode[i] = longtCode[i / 2];
			encode[++i] = latCode[i / 2];
		}

		StringBuilder geoHashCode = new StringBuilder();

		int index = 0;
		for (int i = 0; i < 50; i++) {
			if (encode[i] - 48 == 1) {
				index = index + (1 << (4 - i % 5));
			}
			if ((i + 1) % 5 == 0) {
				geoHashCode.append(BASECODE[index]);
				index = 0;
			}
		}

		return geoHashCode.toString();
	}

	/**
	 * 生成纬度二进制编码
	 * 
	 * @param lat
	 *            纬度
	 * @return 二进制纬度编码串
	 */
	private static char[] getLatCodeString(long lat) {
		char[] latCode = new char[25];

		int l = -90000000, r = 90000000;
		for (int i = 0; i < 25; i++) {
			if (lat < (l + r) / 2) {
				latCode[i] = '0';
				r = (l + r) / 2;
			} else {
				latCode[i] = '1';
				l = (l + r) / 2;
			}
		}
		return latCode;
	}

	/**
	 * 生成经度二进制编码
	 * 
	 * @param lat
	 *            经度
	 * @return 二进制经度编码串
	 */
	private static char[] getLongtCodeString(long longt) {
		char[] longtCode = new char[25];

		int l = -180000000, r = 180000000;
		for (int i = 0; i < 25; i++) {
			if (longt < (l + r) / 2) {
				longtCode[i] = '0';
				r = (l + r) / 2;
			} else {
				longtCode[i] = '1';
				l = (l + r) / 2;
			}
		}
		return longtCode;
	}

}
