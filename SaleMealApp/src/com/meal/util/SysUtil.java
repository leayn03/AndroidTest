package com.meal.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.TimeZone;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Time;
import android.util.Log;

/**
 * @author xiamingxing
 * 
 */
public class SysUtil {

	public static final int NONET = -1;
	public static final int CMNET = 1;
	public static final int CMWAP = 2;
	public static final int WIFI = 3;

	private static String key = "AsDfG2!4^8(hMbVh(j&h%g#g@fDgNbZgF^QdSdQfGhJ:>;)-+-*hGjGh&6%4#5&8hGCXZd!s@d.hJhGjhjd*74387826734273fdska934fdsja8374836fhdsja736437hfdjsa6734aSAjfdkaa12345";

	/**
	 * @return
	 */
	public static String getTime() {

		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。

		t.setToNow(); // 取得系统时间。

		String year = String.valueOf(t.year);
		String month = String.valueOf(t.month + 1);
		String date = String.valueOf(t.monthDay);
		String hour = String.valueOf(t.hour); // 0-23
		String minute = String.valueOf(t.minute);
		String second = String.valueOf(t.second);

		return year + "-" + month + "-" + date + " " + hour + ":" + minute
				+ ":" + second + " ";

	}

	public static String getTimeStamp() {

		Calendar calendar = Calendar.getInstance();// 获取当前日历对象

		long unixTime = calendar.getTimeInMillis();// 获取当前时区下日期时间对应的时间戳

		long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();// 获取标准格林尼治时间下日期时间对应的时间戳

		return String.valueOf(unixTime);

	}

	/**
	 * @param context
	 * @return -1:无网络连接， 1：CMNET 2：CMWAP， 3：WIFI
	 */
	public static int getAPNType(Context context) {

		int netType = -1;

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {

			return netType;

		}
		int nType = networkInfo.getType();

		if (nType == ConnectivityManager.TYPE_MOBILE) {

			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {

				netType = CMNET;

			} else {

				netType = CMWAP;

			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {

			netType = WIFI;

		}

		// netType = NONET;

		return netType;

	}

	/**
	 * @return
	 */
	public static String getLocalIpAddress() {

		String ipaddress = "";

		try {

			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {

				NetworkInterface intf = en.nextElement();

				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {

					InetAddress inetAddress = enumIpAddr.nextElement();

					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(inetAddress
									.getHostAddress())) {

						// ipaddress = ipaddress + ";"
						// + inetAddress.getHostAddress().toString();
						return inetAddress.getHostAddress().toString();

					}

				}

			}

		} catch (SocketException ex) {

			Log.e("WifiPreference IpAddress", ex.toString());

		}

		return null;

	}

	/**
	 * @param key
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getEncodeBase64(String key) {

		String result = "";

		if (null != key) {

			byte[] encode = Base64.encode(key.getBytes());

			result = new String(encode);

		}

		return result;

	}

	/**
	 * @param code
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getDecodeBase64(String code) {

		String result = "";

		if (null != code) {

			byte[] key = Base64.decode(code.getBytes());

			result = new String(key);
		}

		return result;

	}

	/**
	 * @param source
	 * @return
	 */
	public static String getMD5(String string) {

		byte[] source = (string + key).getBytes();

		String s = null;

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };

		try {

			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");

			md.update(source);

			byte tmp[] = md.digest();

			char str[] = new char[16 * 2];

			int k = 0;

			for (int i = 0; i < 16; i++) {

				byte byte0 = tmp[i];

				str[k++] = hexDigits[byte0 >>> 4 & 0xf];

				str[k++] = hexDigits[byte0 & 0xf];

			}

			s = new String(str);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return s;

	}

}
