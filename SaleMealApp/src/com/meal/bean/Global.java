package com.meal.bean;

import org.apache.http.cookie.Cookie;

import com.meal.util.FileCacheUtil;
import com.meal.util.MultiCacheUtil;

/**
 * @author xiamingxing
 * 
 */
public class Global {

	public static FileCacheUtil fileCache = FileCacheUtil.getInstance();

	public static MultiCacheUtil multiCache = MultiCacheUtil.getInstance();

	public static Seller seller = null;

	public static User user = null;
	
	public static String token = null;

}
