package com.falconroid.comm;

public interface ICommListener {
	public void onCommDataIn(byte[] bData,int iStart,int iLength);
	public void onExceptionHappen(String msg);
}