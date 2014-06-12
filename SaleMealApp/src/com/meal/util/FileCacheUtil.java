package com.meal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.meal.bean.Global;

public class FileCacheUtil {

	private static final String DefaultCacheDir = "mealImageCache";

	private static FileCacheUtil instance = null;

	private static final int MAX_CACHE_SIZE = 20 * 1024 * 1024; // 20M

	private static BitmapFactory.Options sBitmapOptions;

	static {

		sBitmapOptions = new BitmapFactory.Options();
		sBitmapOptions.inSampleSize = 4;
		sBitmapOptions.inPurgeable = true; // bitmap can be purged to disk

	}

	/**
	 * @return
	 */
	public static FileCacheUtil getInstance() {

		if (null == instance) {

			instance = new FileCacheUtil();

		}

		File dir = null;

		if (!FileUtils.isFileExist(DefaultCacheDir)) {

			dir = FileUtils.creatSDDir(DefaultCacheDir);

		} else {

			dir = new File(FileUtils.getSDPATH() + DefaultCacheDir);

		}

		instance.setmCacheDir(dir);

		return instance;

	}

	private File mCacheDir = null;

	private LruCache<String, Long> sFileCache = null;

	/**
	 * 
	 */
	private FileCacheUtil() {

		sFileCache = new LruCache<String, Long>(MAX_CACHE_SIZE) {

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Long oldValue, Long newValue) {

				File file = getFile(key);

				if (null != file) {

					file.delete();

				}

			}

			@Override
			public int sizeOf(String key, Long value) {

				return value.intValue();

			}

		};

	}

	/**
	 * @param URL
	 * @return
	 */
	private String _getFileNameFromURL(String URL) {

		String fileName = null;

		if (null != URL) {

			int position = URL.lastIndexOf("/");

			if (-1 != position) {

				fileName = URL.substring(position + 1);

			}

		}

		return fileName;

	}

	// 获取bitmap
	/**
	 * @param key
	 * @return
	 */
	public Bitmap getBitmap(String key) {

		String fileName = _getFileNameFromURL(key);

		File bitmapFile = getFile(fileName);

		Bitmap bitmap = null;

		if (null!=bitmapFile && bitmapFile.exists()) {

			try {

				bitmap = BitmapFactory.decodeStream(new FileInputStream(
						bitmapFile), null, sBitmapOptions);

			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			if (bitmap != null) {

				// 重新将其缓存至硬引用中
				Global.multiCache.putBitmap(key, bitmap);

			}

		}
		return bitmap;

	}

	/**
	 * @param fileName
	 * @return
	 */
	private File getFile(String fileName) {

		File file = null;
		
		if ( null != fileName && !fileName.equals("") ){
			
			file = new File(getmCacheDir(), fileName);

			if (!file.exists() || !file.isFile()) {

				try {

					throw new FileNotFoundException("文件不存在或有同名文件夹");

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}
			
		}	

		return file;

	}

	public File getmCacheDir() {
		return mCacheDir;
	}

	// 根据key获取OutputStream
	private FileOutputStream getOutputStream(String key) {

		FileOutputStream fos = null;

		if (null != getmCacheDir()) {

			try {

				fos = new FileOutputStream(getmCacheDir().getAbsolutePath()
						+ File.separator + key);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();

			}

		}

		return fos;

	}

	// 缓存bitmap到外部存储
	/**
	 * @param key
	 * @param bitmap
	 * @return
	 */
	public boolean putBitmap(String key, Bitmap bitmap) {

		boolean result = false;

		String fileName = _getFileNameFromURL(key);
		
		if ( null != fileName && !fileName.equals("") && null != bitmap){

			File file = getFile(fileName);

			if ( null != file && file.exists()) {

				Log.v("tag", "文件已经存在");

				result = true;

			} else {

				FileOutputStream fos = getOutputStream(fileName);
				
				boolean saved = false;
				
				try{
					
					saved = bitmap.compress(CompressFormat.JPEG, 100, fos);
					
				}
				catch( Exception e ){
					
					e.printStackTrace();
	
				}

				try {

					fos.flush();
					fos.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();

				}

				if (saved) {

					synchronized (sFileCache) {

						sFileCache.put(fileName, getFile(fileName).length());

					}

					result = true;

				}

			}
			
		}

		return result;

	}

	public void setmCacheDir(File mCacheDir) {
		this.mCacheDir = mCacheDir;
	}

}
