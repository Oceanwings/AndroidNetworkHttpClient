package com.erich.android.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import org.apache.http.util.ByteArrayBuffer;

final class SocketTask extends BaseNetworkTask
{
	private static final String SOCKET_PREFIX = "socket://";
	private static final char SEPARATOR = ':';

	private static final int READ_BUFFER_SIZE = 1024;

	private final String mHostAddress;
	private final int mPort;
	private final byte[ ] mData;

	SocketTask( String hostAddress, int port, byte[ ] data, IResponseHandler handler )
	{
		super( handler );

		if ( hostAddress == null || hostAddress.trim( ).length( ) == 0 )
		{
			throw new IllegalArgumentException( "Invalid socket IP address: " + hostAddress );
		}
		else if ( port < 0 )
		{
			throw new IllegalArgumentException( "Invalid socket port: " + port );
		}
		else if ( data == null || data.length == 0 )
		{
			throw new IllegalArgumentException( "Data bytes should not be empty." );
		}

		this.mHostAddress = hostAddress.trim( );
		this.mPort = port;
		this.mData = Arrays.copyOf( data, data.length );
	}

	SocketTask( String hostAddressPort, byte[ ] data, IResponseHandler handler )
	{
		super( handler );

		if ( hostAddressPort == null || hostAddressPort.trim( ).length( ) == 0 )
		{
			throw new IllegalArgumentException( "Invalid socket address&port string: "
					+ hostAddressPort );
		}

		hostAddressPort = hostAddressPort.trim( );
		int index = hostAddressPort.indexOf( SEPARATOR );
		if ( index == -1 )
		{
			throw new IllegalArgumentException( "Invalid socket address&port string: "
					+ hostAddressPort );
		}

		final String address = hostAddressPort.substring( 0, index );
		final int port;
		try
		{
			port = Integer.parseInt( hostAddressPort.substring( index + 1 ) );
		}
		catch ( NumberFormatException nfe )
		{
			nfe.printStackTrace( );
			throw new IllegalArgumentException( "Invalid socket address&port string: "
					+ hostAddressPort, nfe );
		}

		this.mHostAddress = address.trim( );
		this.mPort = port;
		this.mData = Arrays.copyOf( data, data.length );
	}

	@Override
	public void run( )
	{
		Socket socket = new Socket( );
		BufferedOutputStream os = null;
		BufferedInputStream ins = null;
		try
		{
			socket.connect( new InetSocketAddress( this.mHostAddress, this.mPort ), 300000 );

			if ( socket.isClosed( ) || !socket.isConnected( ) )
			{
				this.notifyNetworkFailed( SocketStatus.NOT_CONNECTED, this.getRequestURI( ) );
				return;
			}

			// Send data.
			if ( socket.isClosed( ) || socket.isOutputShutdown( ) )
			{
				this.notifyNetworkFailed( SocketStatus.OUTPUT_SHUTDOWN, this.getRequestURI( ) );
				return;
			}

			os = new BufferedOutputStream( socket.getOutputStream( ), this.mData.length );
			ins = new BufferedInputStream( socket.getInputStream( ) );

			os.write( this.mData );
			os.flush( );

			final long time = System.currentTimeMillis( );

			// Read response.
			if ( socket.isClosed( ) || socket.isInputShutdown( ) )
			{
				this.notifyNetworkFailed( SocketStatus.INPUT_SHUTDOWN, this.getRequestURI( ) );
				return;
			}

			// Default buffer size of BufferedInputStream is 8192.
			final int totalLength = ins.available( );
			final byte[ ] readBuffer = new byte[ READ_BUFFER_SIZE ];
			final ByteArrayBuffer buffer = new ByteArrayBuffer( totalLength );
			int readBytes = 0;
			while ( ( readBytes = ins.read( readBuffer ) ) != -1 )
			{
				buffer.append( readBuffer, 0, readBytes );
				Arrays.fill( readBuffer, ( byte ) 0 );
			}

			System.out.println( System.currentTimeMillis( ) - time );

			this.notifyNetworkSucceed( buffer.toByteArray( ) );
		}
		catch ( UnknownHostException e )
		{
			e.printStackTrace( );
			this.notifyNetworkException( e );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
			this.notifyNetworkException( e );
		}
		finally
		{
			if ( os != null )
			{
				try
				{
					os.close( );
				}
				catch ( IOException e )
				{
					e.printStackTrace( );
				}
			}

			if ( ins != null )
			{
				try
				{
					ins.close( );
				}
				catch ( IOException e )
				{
					e.printStackTrace( );
				}
			}

			try
			{
				socket.close( );
			}
			catch ( IOException e )
			{
				e.printStackTrace( );
			}
		}
	}

	private URI getRequestURI( )
	{
		URI uri = null;
		try
		{
			uri = new URI( SOCKET_PREFIX, "", this.mHostAddress, this.mPort, "", "", "" );
		}
		catch ( URISyntaxException e )
		{
			e.printStackTrace( );
			uri = null;
		}

		return uri;
	}
}