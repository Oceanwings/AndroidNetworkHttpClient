package com.erich.android.network;

import java.net.URI;

abstract class BaseNetworkTask implements Runnable
{
	static final int HASH_PRIME = 31;

	private final IResponseHandler mHandler;

	BaseNetworkTask( IResponseHandler handler )
	{
		this.mHandler = handler;
	}

	final void notifyNetworkSucceed( byte[ ] data )
	{
		if ( this.mHandler != null )
		{
			this.mHandler.networkSucceed( data );
		}
	}

	final void notifyNetworkFailed( final int statusCode, URI requestURI )
	{
		if ( this.mHandler != null )
		{
			this.mHandler.networkFailed( statusCode, requestURI );
		}
	}

	final void notifyNetworkException( Throwable throwable )
	{
		if ( this.mHandler != null )
		{
			this.mHandler.networkException( throwable );
		}
	}

	@Override
	public int hashCode( )
	{
		return HASH_PRIME + ( ( this.mHandler == null ) ? 0 : this.mHandler.hashCode( ) );
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
		{
			return true;
		}
		else if ( !( obj instanceof BaseNetworkTask ) )
		{
			return false;
		}
		else
		{
			BaseNetworkTask other = ( BaseNetworkTask ) obj;
			return ( this.mHandler == other.mHandler );
		}
	}
}