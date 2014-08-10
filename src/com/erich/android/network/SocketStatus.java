package com.erich.android.network;

public final class SocketStatus
{
	public static final int NOT_CONNECTED = 0x4000001;
	public static final int NOT_BONDED = 0x4000002;
	public static final int OUTPUT_SHUTDOWN = 0x4000100;
	public static final int INPUT_SHUTDOWN = 0x4000101;

	private SocketStatus( )
	{}
}