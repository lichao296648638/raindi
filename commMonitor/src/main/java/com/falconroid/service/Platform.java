package com.falconroid.service;

import android.util.Log;

import com.huayu.io.EMgpio;

public class Platform {
	private static final int PIN_BARCODE_POWER = 85;
	private static final int PIN_BARCODE_CTS = 84;
	private static final int PIN_BARCODE_TRIG = 86;
	
	private static final int PIN_RFID_POWER = 70;
	private static final int PIN_RFID_BOOT = 76;
	
	private static final int PIN_UHF_POWER = 76;
	
	private static final int PIN_TAG_POWER = 70;
	
	private static boolean mIoInit = false;
	
		
	public static void initIO(){
		if(!mIoInit){			
			if(EMgpio.GPIOInit()){
				Log.d("Platform", "InitIO succ");
				mIoInit = true;
				return;
			}else{
				Log.d("Platform", "InitIO fail");
				return;
			}
		}
	}
	
	public static void deInitIO(){
		if(mIoInit){
			if(EMgpio.GPIOUnInit()){
				mIoInit = false;
			}
		}
	}
	
	//barcode io
	public static void openBarcodePower(){
		
		EMgpio.SetGpioOutput(PIN_BARCODE_POWER);
		EMgpio.SetGpioOutput(PIN_BARCODE_CTS);
		EMgpio.SetGpioOutput(PIN_BARCODE_TRIG);

		EMgpio.SetGpioDataHigh(PIN_BARCODE_POWER);
		EMgpio.SetGpioDataHigh(PIN_BARCODE_CTS);
		EMgpio.SetGpioDataHigh(PIN_BARCODE_TRIG);
	}
	
	public static void closeBarcodePower(){
		EMgpio.SetGpioOutput(PIN_BARCODE_POWER);
		EMgpio.SetGpioOutput(PIN_BARCODE_CTS);
		EMgpio.SetGpioOutput(PIN_BARCODE_TRIG);

		EMgpio.SetGpioDataLow(PIN_BARCODE_POWER);
		EMgpio.SetGpioDataHigh(PIN_BARCODE_CTS);
		EMgpio.SetGpioDataHigh(PIN_BARCODE_TRIG);
		
	}
	
	public static void trigOn(){

		EMgpio.SetGpioOutput(PIN_BARCODE_TRIG);
		EMgpio.SetGpioDataHigh(PIN_BARCODE_TRIG);

		for(int i=0; i<100000;i++){;}
	
		EMgpio.SetGpioDataLow(PIN_BARCODE_TRIG);
	}
	
	public static void trigOff(){
		EMgpio.SetGpioOutput(PIN_BARCODE_TRIG);
		EMgpio.SetGpioDataHigh(PIN_BARCODE_TRIG);		
	}
	
	//boot mode
	//boot mode
	private static void setBoot(){
		EMgpio.SetGpioOutput(PIN_RFID_BOOT);
		EMgpio.SetGpioDataLow(PIN_RFID_BOOT);
	}
	
	private static void cleanBoot(){
		EMgpio.SetGpioOutput(PIN_RFID_BOOT);
		EMgpio.SetGpioDataHigh(PIN_RFID_BOOT);
	}

	public static void enterBootMode(){
		setBoot();
		EMgpio.SetGpioOutput(PIN_RFID_POWER);
		EMgpio.SetGpioDataHigh(PIN_RFID_POWER);
	}
	
	//tag125k io
	public static void openTagPower(){		
		EMgpio.SetGpioOutput(PIN_TAG_POWER);
		EMgpio.SetGpioDataHigh(PIN_TAG_POWER);
	}
	
	public static void closeTagPower(){
		EMgpio.SetGpioOutput(PIN_TAG_POWER);
		EMgpio.SetGpioDataLow(PIN_TAG_POWER);
	}
	
	//rfid io
	public static void openRfidPower(){
		cleanBoot();
		
		EMgpio.SetGpioOutput(PIN_RFID_POWER);
		EMgpio.SetGpioDataHigh(PIN_RFID_POWER);		
	}
	
	public static void closeRfidPower(){
		EMgpio.SetGpioOutput(PIN_RFID_POWER);
		EMgpio.SetGpioDataLow(PIN_RFID_POWER);
		cleanBoot();
	}
		
	//uhf io
	public static void openUhfPower(){
		EMgpio.SetGpioOutput(PIN_UHF_POWER);
		EMgpio.SetGpioDataHigh(PIN_UHF_POWER);
	}
	
	public static void closeUhfPower(){
		EMgpio.SetGpioOutput(PIN_UHF_POWER);
		EMgpio.SetGpioDataLow(PIN_UHF_POWER);
	}
	
	public static boolean SetGpioInput(int gpio_index){
		return EMgpio.SetGpioInput(gpio_index);
	}
	
	public static boolean SetGpioOutput(int gpio_index){
		return EMgpio.SetGpioOutput(gpio_index);
	}
	
	public static boolean SetGpioDataHigh(int gpio_index){
		return EMgpio.SetGpioDataHigh(gpio_index);
	}
	
	public static boolean SetGpioDataLow(int gpio_index){
		return EMgpio.SetGpioDataLow(gpio_index);
	}
}
