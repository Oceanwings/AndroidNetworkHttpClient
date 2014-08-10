package com.erich.android.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

final class NewPaySSLSocketFactory extends SSLSocketFactory
{
	private static final String DEFAULT_SSL_PROTOCOL = "SSL"; // TSL

	private final SSLContext mSSLContext = SSLContext.getInstance( DEFAULT_SSL_PROTOCOL );

	NewPaySSLSocketFactory( KeyStore truststore ) throws NoSuchAlgorithmException,
			KeyManagementException, KeyStoreException, UnrecoverableKeyException
	{
		super( truststore );

		this.mSSLContext.init( null, new TrustManager[ ] { new X509TrustManager( ) {
			@Override
			public X509Certificate[ ] getAcceptedIssuers( )
			{
				return null;
			}

			@Override
			public void checkServerTrusted( X509Certificate[ ] chain, String authType )
					throws CertificateException
			{}

			@Override
			public void checkClientTrusted( X509Certificate[ ] chain, String authType )
					throws CertificateException
			{}
		} }, null );
	}

	@Override
	public Socket createSocket( ) throws IOException
	{
		return this.mSSLContext.getSocketFactory( ).createSocket( );
	}

	@Override
	public Socket createSocket( Socket socket, String host, int port, boolean autoClose )
			throws IOException, UnknownHostException
	{
		return this.mSSLContext.getSocketFactory( ).createSocket( socket, host, port, autoClose );
	}
}