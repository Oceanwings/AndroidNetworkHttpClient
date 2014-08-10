package com.erich.android.network;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

final class HttpGetTask extends BaseHttpTask
{
	HttpGetTask( String requestUrl, IResponseHandler handler )
	{
		super( requestUrl, handler );
	}

	@Override
	HttpRequestBase createHttpTask( String requestUrl )
	{
		return new HttpGet( requestUrl );
	}

	@Override
	public boolean equals( Object obj )
	{
		return ( obj != null && HttpGetTask.class == obj.getClass( ) ) ? super.equals( obj )
				: false;
	}

	@Override
	public int hashCode( )
	{
		return super.hashCode( ) * HASH_PRIME + HttpGetTask.class.getName( ).hashCode( );
	}
}