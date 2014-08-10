package com.erich.android.network;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;

public final class RequestManager
{
	private static final int DEFAULT_THREAD_POOL_SIZE = 1;

	public static RequestManager newInstance( )
	{
		return new RequestManager( );
	}

	private ExecutorService mNetworkService = Executors
			.newFixedThreadPool( DEFAULT_THREAD_POOL_SIZE );

	private RequestManager( )
	{}

	private synchronized void submitTask( final Runnable networkTask )
	{
		this.mNetworkService.submit( networkTask );
	}

	public void sendPostRequest( String url, byte[ ] data, IResponseHandler responseHandler )
	{
		this.submitTask( new ByteArrayHttpPostTask( url, data, responseHandler ) );
	}

	public void sendPostRequest( String url, List< NameValuePair > data,
			IResponseHandler responseHandler )
	{
		this.submitTask( new FormHttpPostTask( url, data, responseHandler ) );
	}

	public void sendPostRequest( String url, IResponseHandler responseHandler )
	{
		this.submitTask( new BasicHttpPostTask( url, responseHandler ) );
	}

	public void sendGetRequest( String url, IResponseHandler handler )
	{
		this.submitTask( new HttpGetTask( url, handler ) );
	}

	public void doSocket( String targetAddress, int port, byte[ ] data, IResponseHandler handler )
	{
		this.submitTask( new SocketTask( targetAddress, port, data, handler ) );
	}

	public void doSocket( String socketAddressPort, byte[ ] data, IResponseHandler handler )
	{
		this.submitTask( new SocketTask( socketAddressPort, data, handler ) );
	}

	public synchronized void close( )
	{
		this.mNetworkService.shutdown( );
	}

	public synchronized void forcedClose( )
	{
		this.mNetworkService.shutdownNow( );
	}
}