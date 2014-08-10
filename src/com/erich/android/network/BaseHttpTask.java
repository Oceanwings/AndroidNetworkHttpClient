package com.erich.android.network;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import android.util.Log;

abstract class BaseHttpTask extends BaseNetworkTask
{
	private static final String LOG_TAG = BaseHttpTask.class.getName( );

	private String mRequestUrl;

	BaseHttpTask( String requestUrl, IResponseHandler handler )
	{
		super( handler );

		if ( isValidUrl( requestUrl ) )
		{
			this.mRequestUrl = requestUrl;
		}
		else
		{
			throw new IllegalArgumentException( );
		}
	}

	@Override
	public void run( )
	{
		this.sendRequest( this.createHttpTask( this.mRequestUrl ) );
	}

	abstract HttpRequestBase createHttpTask( String requestUrl );

	private void sendRequest( HttpRequestBase httpRequest )
	{
		ISender sender = SenderFactory.newInstance( this.mRequestUrl );
		try
		{
			HttpResponse response = sender.execute( httpRequest );
			if ( response != null )
			{
				final int statusCode = response.getStatusLine( ).getStatusCode( );
				Log.d( LOG_TAG, "Http Response Code: " + statusCode );

				if ( statusCode == HttpStatus.SC_OK )
				{
					this.notifyNetworkSucceed( EntityUtils.toByteArray( response.getEntity( ) ) );
				}
				else
				{
					this.notifyNetworkFailed( statusCode, httpRequest.getURI( ) );
				}
			}
		}
		catch ( IOException e )
		{
			this.notifyNetworkException( e );
			Log.d( this.getClass( ).getName( ), e.getMessage( ), e );
			e.printStackTrace( );
		}
		finally
		{
			sender.shutdown( );
		}
	}

	private static boolean isValidUrl( String requestUrl )
	{
		// FIXME: use regular expression for validation check.
		return ( requestUrl != null && requestUrl.length( ) > 0 );
	}

	@Override
	public int hashCode( )
	{
		int result = super.hashCode( ) * HASH_PRIME + BaseHttpTask.class.getName( ).hashCode( );
		result *= HASH_PRIME;
		result += this.mRequestUrl.hashCode( );
		result *= HASH_PRIME;
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( obj == this )
		{
			return true;
		}
		else if ( !( obj instanceof BaseHttpTask ) || !super.equals( obj ) )
		{
			return false;
		}
		else
		{
			BaseHttpTask task = ( BaseHttpTask ) obj;
			return equals( this.mRequestUrl, task.mRequestUrl );
		}
	}

	private static boolean equals( String str1, String str2 )
	{
		if ( str1 == null )
		{
			return str2 == null;
		}

		return str1.equals( str2 );
	}
}