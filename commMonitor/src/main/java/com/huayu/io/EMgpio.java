package com.huayu.io;

public class EMgpio
{
	public static native int GetGpioMaxNumber();
	
	public static native boolean GPIOInit();
	public static native boolean GPIOUnInit();	
	public static native boolean SetGpioInput(int gpio_index);
	public static native boolean SetGpioOutput(int gpio_index);
	public static native boolean SetGpioDataHigh(int gpio_index);
	public static native boolean SetGpioDataLow(int gpio_index);
	
	public static native int GetCurrent(int hostNumber);
	public static native int NewGetCurrent(int hostNumber);
	public static native boolean SetCurrent(int hostNumber, int currentDataIdx, int currentCmdIdx);
	public static native boolean NewSetCurrent(int hostNumber, int clkpu, int clkpd, int cmdpu, int cmdpd, int datapu, int datapd);
	static {

		System.loadLibrary("hy_gpio_jni");
		
	}
}
