package com.meal.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * @author xiamingxing
 * 
 */
public class LruCacheUtil {

	private static LruCacheUtil instance = null;

	/**
	 * @return
	 */
	public static LruCacheUtil getInstance() {

		if (null == instance) {

			instance = new LruCacheUtil();

		}

		return instance;

	}

	private LruCache<String, Bitmap> mMemoryCache = null;

	private LruCacheUtil() {

		int cacheSize = getCacheSize();

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize);

	}

	/**
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {

		if (getBitmapFromMemCache(key) == null) {

			mMemoryCache.put(key, bitmap);

		}

	}

	/**
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemCache(String key) {

		return mMemoryCache.get(key);

	}

	/**
	 * @return
	 */
	private int getCacheSize() {

		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		int cacheSize = maxMemory / 8;

		return cacheSize;

	}
}
