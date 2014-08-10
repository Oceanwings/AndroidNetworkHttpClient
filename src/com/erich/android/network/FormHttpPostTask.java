package com.erich.android.network;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import android.util.Log;

final class FormHttpPostTask extends BaseHttpPostTask
{
	private static final String LOG_TAG = FormHttpPostTask.class.getName( );

	private static final List< NameValuePair > EMPTY_LIST = Collections
			.unmodifiableList( new ArrayList< NameValuePair >( 0 ) );

	private final List< NameValuePair > mData;

	FormHttpPostTask( String requestUrl, List< NameValuePair > data, IResponseHandler handler )
	{
		super( requestUrl, handler );

		this.mData = ( data != null ) ? Collections.unmodifiableList( data ) : EMPTY_LIST;
	}

	@Override
	HttpEntity createPostDataEntity( )
	{
		HttpEntity entity = null;
		try
		{
			entity = new UrlEncodedFormEntity( this.mData, "UTF-8" );
		}
		catch ( UnsupportedEncodingException e )
		{
			e.printStackTrace( );
			Log.d( LOG_TAG, e.getMessage( ) );

			entity = null;
		}
		return entity;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( obj == this )
		{
			return true;
		}
		else if ( obj == null || FormHttpPostTask.class != obj.getClass( ) || !super.equals( obj ) )
		{
			return false;
		}

		List< NameValuePair > otherList = ( ( FormHttpPostTask ) obj ).mData;
		final int size = this.mData.size( );
		if ( size != otherList.size( ) )
		{
			return false;
		}

		boolean equal = true;
		for ( int i = 0; equal && i < size; ++i )
		{
			equal = equal( this.mData.get( i ), otherList.get( i ) );
		}
		return equal;
	}

	@Override
	public int hashCode( )
	{
		int result = super.hashCode( ) * HASH_PRIME + FormHttpPostTask.class.getName( ).hashCode( );
		result *= HASH_PRIME;
		result += this.mData.hashCode( );
		return result;
	}

	private static boolean equal( NameValuePair pair_1, NameValuePair pair_2 )
	{
		if ( pair_1 == pair_2 )
		{
			return true;
		}
		else if ( pair_1 != null )
		{
			return pair_1.equals( pair_2 );
		}
		else
		{
			return false;
		}
	}
}