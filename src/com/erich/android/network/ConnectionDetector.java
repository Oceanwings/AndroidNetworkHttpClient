package com.erich.android.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectionDetector
{
	private static final String ERROR_MSG_NOT_INITIALIZED = "ConnectionDetector is not initiazlied!";

	public static ConnectionDetector newInstance( Context context )
	{
		return ( context != null ) ? new ConnectionDetector( context ) : null;
	}

	private final ConnectivityManager mConnManager;

	private ConnectionDetector( Context context )
	{
		this.mConnManager = ( ConnectivityManager ) context
				.getSystemService( Context.CONNECTIVITY_SERVICE );
	}

	private boolean isInitialized( )
	{
		return ( this.mConnManager != null );
	}

	private void checkInitalize( )
	{
		if ( !this.isInitialized( ) )
		{
			throw new RuntimeException( ERROR_MSG_NOT_INITIALIZED );
		}
	}

	public int getActiveNetworkType( )
	{
		this.checkInitalize( );

		final NetworkInfo activeNetwork = this.mConnManager.getActiveNetworkInfo( );
		return ( activeNetwork != null && activeNetwork.isAvailable( ) ) ? activeNetwork.getType( )
				: -1;
	}

	public boolean isNetworkAvailable( )
	{
		this.checkInitalize( );

		final NetworkInfo activeNetwork = this.mConnManager.getActiveNetworkInfo( );
		return ( activeNetwork != null ) ? activeNetwork.isAvailable( ) : false;
	}

	public boolean isNetworkConnected( )
	{
		this.checkInitalize( );

		final NetworkInfo activeNetwork = this.mConnManager.getActiveNetworkInfo( );
		return ( activeNetwork != null ) ? activeNetwork.isAvailable( )
				&& activeNetwork.isConnected( ) : false;
	}

	public boolean isMobileNetworkkAvailable( )
	{
		return isNetworkAvailable( ConnectivityManager.TYPE_MOBILE );
	}

	public boolean isWifiAvailable( )
	{
		return isNetworkAvailable( ConnectivityManager.TYPE_WIFI );
	}

	private boolean isNetworkAvailable( final int networkType )
	{
		this.checkInitalize( );

		final NetworkInfo activeNetwork = this.mConnManager.getNetworkInfo( networkType );
		return ( activeNetwork != null ) ? activeNetwork.isAvailable( )
				&& activeNetwork.isConnected( ) : false;
	}
}