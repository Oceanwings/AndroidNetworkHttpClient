package com.erich.android.network;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpConnectionParams;
import android.net.http.AndroidHttpClient;

final class AndroidHttpSender implements ISender
{
	private static final String HTTP_USER_AGENT = "Android";

	private AndroidHttpSender( )
	{}

	private static final int TIME_OUT_CONNECTION = 90 * 1000;
	private static final int TIME_OUT_SO = 90 * 1000;

	private HttpClient mHttpClient;

	private void initHttpParams( )
	{
		HttpConnectionParams.setConnectionTimeout( this.mHttpClient.getParams( ),
				TIME_OUT_CONNECTION );
		HttpConnectionParams.setSoTimeout( this.mHttpClient.getParams( ), TIME_OUT_SO );
	}

	public HttpResponse execute( HttpUriRequest request ) throws IOException
	{
		this.mHttpClient = this.createHttpClient( );
		if ( this.mHttpClient != null )
		{
			this.initHttpParams( );
			return this.mHttpClient.execute( request );
		}
		else
		{
			return null;
		}
	}

	public void shutdown( )
	{
		shutdownHttpClient( this.mHttpClient );
	}

	private HttpClient createHttpClient( )
	{
		return AndroidHttpClient.newInstance( HTTP_USER_AGENT );
	}

	private static void shutdownHttpClient( HttpClient httpClient )
	{
		if ( httpClient != null )
		{
			httpClient.getConnectionManager( ).shutdown( );
			( ( AndroidHttpClient ) httpClient ).close( );
		}
	}

	static AndroidHttpSender newHttpSender( )
	{
		return new AndroidHttpSender( );
	}
}