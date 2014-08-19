package com.example.base;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class BaseHttp extends AsyncHttpClient {
	private ResponseHandlerInterface mResponseHandler;
	public BaseHttp(final Context mContext) {
	}
	public BaseHttp(Context mContext,ResponseHandlerInterface responseHandler) {
		this.mResponseHandler = responseHandler;
	}
	@Override
	public RequestHandle post(String url, RequestParams params,
			ResponseHandlerInterface responseHandler) {
		if(responseHandler==null){
			responseHandler = mResponseHandler;
		}
		return super.post(url, params, responseHandler);
	}
	
	@Override
	public RequestHandle post(String url,
			ResponseHandlerInterface responseHandler) {
		if(responseHandler==null){
			responseHandler = mResponseHandler;
		}
		return super.post(url, responseHandler);
	}
}
