package com.erich.android.network;

final class SenderFactory
{
	private static final String HTTPS_PREFIX = "https";

	private SenderFactory( )
	{}

	static ISender newInstance( String requestUrl )
	{
		return useHttps( requestUrl ) ? AndroidHttpsSender.newHttpsSender( ) : AndroidHttpSender
				.newHttpSender( );
	}

	private static boolean useHttps( String requestUrl )
	{
		return ( requestUrl != null ) && requestUrl.startsWith( HTTPS_PREFIX );
	}
}