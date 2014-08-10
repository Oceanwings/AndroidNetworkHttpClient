package com.erich.android.network;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;

final class BasicHttpPostTask extends BaseHttpPostTask
{
	BasicHttpPostTask( String requestUrl, IResponseHandler handler )
	{
		super( requestUrl, handler );
	}

	@Override
	HttpEntity createPostDataEntity( )
	{
		return new BasicHttpEntity( );
	}

	@Override
	public boolean equals( Object obj )
	{
		return ( obj != null && BasicHttpPostTask.class == obj.getClass( ) ) ? super.equals( obj )
				: false;
	}

	@Override
	public int hashCode( )
	{
		return super.hashCode( ) * HASH_PRIME + BasicHttpPostTask.class.getName( ).hashCode( );
	}
}