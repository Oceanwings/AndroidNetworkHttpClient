package com.erich.android.network;

import java.io.IOException;
import java.security.KeyStore;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

final class AndroidHttpsSender implements ISender
{
	private static final String HTTP = "http";
	private static final String HTTPS = "https";

	private static final int HTTP_PORT = 80;
	private static final int HTTPS_PORT = 443;

	private static final int DEFAULT_TIMEOUT = 10000;

	static AndroidHttpsSender newHttpsSender( )
	{
		return new AndroidHttpsSender( );
	}

	private AndroidHttpsSender( )
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
		HttpClient httpClient = null;
		try
		{
			final HttpParams httpParams = createHttpParams( );
			httpClient = new DefaultHttpClient( new ThreadSafeClientConnManager( httpParams,
					createSchemeRegistry( ) ), httpParams );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			httpClient = null;
		}

		return httpClient;
	}

	private static void shutdownHttpClient( HttpClient httpClient )
	{
		if ( httpClient != null )
		{
			httpClient.getConnectionManager( ).shutdown( );
		}
	}

	private static HttpParams createHttpParams( )
	{
		HttpParams httpParams = new BasicHttpParams( );
		HttpProtocolParams.setVersion( httpParams, HttpVersion.HTTP_1_1 );
		HttpProtocolParams.setContentCharset( httpParams, "UTF-8" );
		HttpProtocolParams.setUseExpectContinue( httpParams, false );

		ConnManagerParams.setTimeout( httpParams, DEFAULT_TIMEOUT );
		HttpConnectionParams.setConnectionTimeout( httpParams, DEFAULT_TIMEOUT );
		HttpConnectionParams.setSocketBufferSize( httpParams, DEFAULT_TIMEOUT );
		return httpParams;
	}

	private static SchemeRegistry createSchemeRegistry( ) throws Exception
	{
		KeyStore trustStore = KeyStore.getInstance( KeyStore.getDefaultType( ) );
		trustStore.load( null, null );

		SSLSocketFactory sslFactory = new NewPaySSLSocketFactory( trustStore );
		sslFactory.setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );

		SchemeRegistry schemeReg = new SchemeRegistry( );
		schemeReg.register( new Scheme( HTTP, PlainSocketFactory.getSocketFactory( ), HTTP_PORT ) );
		schemeReg.register( new Scheme( HTTPS, sslFactory, HTTPS_PORT ) );
		return schemeReg;
	}
}