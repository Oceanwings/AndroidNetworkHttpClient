package com.erich.android.network;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

interface ISender
{
	HttpResponse execute( HttpUriRequest request ) throws IOException;

	void shutdown( );
}