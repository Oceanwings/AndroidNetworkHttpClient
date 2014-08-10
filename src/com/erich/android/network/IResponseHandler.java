package com.erich.android.network;

import java.net.URI;

public interface IResponseHandler
{
	public void networkSucceed( byte[ ] responseData );

	public void networkFailed( final int statusCode, URI requestURI );

	public void networkException( Throwable throwable );
}