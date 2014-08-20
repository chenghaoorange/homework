package com.example.base;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.view.LayoutInflater;

import com.example.homework.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class BaseApplication extends Application {
	public static LayoutInflater mInflater;
	public static DisplayImageOptions displayImageOptions;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
		java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
		File cacheDir = StorageUtils.getCacheDirectory(this);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.discCacheExtraOptions(480, 800, CompressFormat.PNG, 75, null)
				.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				// default
				.discCache(new UnlimitedDiscCache(cacheDir))
				// default
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(this)) // default
				// .imageDecoder(new BaseImageDecoder()) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();
		displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ImageLoader.getInstance().init(config);
	}
}
