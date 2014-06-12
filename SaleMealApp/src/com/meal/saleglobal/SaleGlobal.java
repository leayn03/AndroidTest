package com.meal.saleglobal;

import com.meal.bean.Seller;
import com.meal.util.FileCacheUtil;
import com.meal.util.MultiCacheUtil;

/**
 * 
 * 
 */
public class SaleGlobal {

	public static FileCacheUtil fileCache = FileCacheUtil.getInstance();

	public static MultiCacheUtil multiCache = MultiCacheUtil.getInstance();

	public static Seller seller = null;


}