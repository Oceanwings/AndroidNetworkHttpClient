package com.erich.android.network;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

abstract class BaseHttpPostTask extends BaseHttpTask
{
	BaseHttpPostTask( String requestUrl, IResponseHandler handler )
	{
		super( requestUrl, handler );
	}

	@Override
	HttpRequestBase createHttpTask( String requestUrl )
	{
		HttpPost httpPost = new HttpPost( requestUrl );
		httpPost.setEntity( this.createPostDataEntity( ) );
		return httpPost;
	}

	abstract HttpEntity createPostDataEntity( );
}