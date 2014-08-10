package com.erich.android.network;

import java.util.Arrays;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

final class ByteArrayHttpPostTask extends BaseHttpPostTask
{
	private static final byte[ ] EMPTY_DATA = new byte[ 0 ];

	private final byte[ ] mData;

	ByteArrayHttpPostTask( String requestUrl, byte[ ] data, IResponseHandler handler )
	{
		super( requestUrl, handler );

		if ( data != null && data.length > 0 )
		{
			this.mData = new byte[ data.length ];
			System.arraycopy( data, 0, this.mData, 0, data.length );
		}
		else
		{
			this.mData = EMPTY_DATA;
		}
	}

	@Override
	HttpEntity createPostDataEntity( )
	{
		return new ByteArrayEntity( this.mData );
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( obj == this )
		{
			return true;
		}
		else if ( obj == null || ByteArrayHttpPostTask.class != obj.getClass( )
				|| !super.equals( obj ) )
		{
			return false;
		}

		return Arrays.equals( this.mData, ( ( ByteArrayHttpPostTask ) obj ).mData );
	}

	@Override
	public int hashCode( )
	{
		int result = super.hashCode( ) * HASH_PRIME
				+ ByteArrayHttpPostTask.class.getName( ).hashCode( );

		result *= HASH_PRIME;
		result += this.mData.hashCode( );
		return result;
	}
}