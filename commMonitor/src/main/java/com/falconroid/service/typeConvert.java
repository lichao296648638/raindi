package com.falconroid.service;

public class typeConvert {
	private static byte[] bshort = new byte[2];
	private static byte[] bint = new byte[4];
	
	public static String ByteToNumberString(byte[] bData){
		if(bData == null){
			return null;
		}
		
		if(bData.length == 0){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<bData.length; i++){
			if((bData[i] >= '0')&&(bData[i] <= '9')){
				sb.append(bData[i]-'0');
			}else{
				return null;
			}
		}
		
		return sb.toString();
	}
	
	public static String ByteToString(byte[] bData){
		if(bData == null){
			return null;
		}
		
		if(bData.length == 0){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<bData.length; i++){
			if((bData[i] >= '0')&&(bData[i] <= '9')){
				sb.append(bData[i]-'0');
			}else{
				sb.append((char)bData[i]);
			}
		}
		
		return sb.toString();
	}
	
	public static String ByteToHexString(byte[] bData){
		if(bData == null){
			return null;
		}
		
		if(bData.length == 0){
			return null;
		}
		
		boolean bZeroFlag = true;
						
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<bData.length; i++){
			if(bData[i] != 0x00){
				bZeroFlag = false;
			}
			sb.append(Integer.toHexString(bData[i]&0xFF));
		}
		
		if(bZeroFlag){
			return null;
		}
		
		return sb.toString();
	}
	
	public static void test(){
		int src = 0x12345678;
		int translate;
		System.out.println("Translate int...........");
		byte[] bData = typeConvert.int2byte(src);
		for(int i=0; i<bData.length; i++){
			System.out.println(Integer.toHexString(bData[i]&0xFF));
		}
		translate = typeConvert.byte2int(bData, 0);
		System.out.println("translate:"+Integer.toHexString(translate));
		
		System.out.println("Translate short.......");
		bData = typeConvert.short2byte(src);
		for(int i=0; i<bData.length; i++){
			System.out.println(Integer.toHexString(bData[i]&0xFF));
		}
		translate = typeConvert.byte2short(bData, 0);
		System.out.println("translate:"+Integer.toHexString(translate));
	}
	
	public static byte[] short2byte(int n){
		//phone the same
		//bshort[0] = (byte)(n>>8);
		//bshort[1] = (byte)n;
		//big endian
		bshort[0] = (byte)(n>>8);
		bshort[1] = (byte)n;
		return bshort;
	}
	
	public static byte[] int2byte(int n){
		//bint[0] = (byte)(n>>24);
		//bint[1] = (byte)(n>>16);
		//bint[2] = (byte)(n>>8);
		//bint[3] = (byte)n;
		bint[3] = (byte)(n>>24);
		bint[2] = (byte)(n>>16);
		bint[1] = (byte)(n>>8);
		bint[0] = (byte)n;
		return bint;
	}
	public static byte acciihextobyte(byte value) {
		StringBuffer s = new StringBuffer();
		String first = String.valueOf(value/16);
		String seconed = String.valueOf(value%16);
		s.append(first);
		s.append(seconed);
		byte iValue = (byte)Integer.parseInt(s.toString());
		return iValue;
	}
	public static short byte2short(byte[] bbyte,int index){
		int iValue = 0;
		//pc
		//iValue = (bbyte[index+1]<<8) + (bbyte[index]&0x000000FF);
		//big endian
		iValue = (bbyte[index]<<8) + (bbyte[index+1]&0x000000FF);
		return (short)iValue;
	}
	
	public static int byte2int(byte[] bbyte,int index){
		int iValue = 0;
		//pc
		//iValue = ((bbyte[index+3]<<24)&0xFF000000) + ((bbyte[index+2]<<16)&0x00FF0000) + ((bbyte[index+1]<<8)&0x0000FF00) + ((bbyte[index])&0x000000FF);
		//big endian
		iValue = ((bbyte[index]<<24)&0xFF000000) + ((bbyte[index+1]<<16)&0x00FF0000) + ((bbyte[index+2]<<8)&0x0000FF00) + ((bbyte[index+3])&0x000000FF);
		return iValue;
	}
	
	public static byte[] HexToAscii(byte bValue){
		byte[] bData = new byte[2];
		int iValue = bValue&0xFF;
		
		bData[0] = (byte)(iValue/16);
		bData[1] = (byte)(iValue%16);
		
		if(bData[0] < 10){
			bData[0] = (byte)(bData[0] + 0x30);
		}else{
			bData[0] = (byte)(bData[0] + 'A' -10);
		}
		
		if(bData[1] < 10){
			bData[1] = (byte)(bData[1] + 0x30);
		}else{
			bData[1] = (byte)(bData[1] + 'A' -10);
		}
		
		return bData;
	}
	
	public static byte AsciiToHex(byte bDataHigh, byte bDataLow){
		int iValue = 0;
		
		if((bDataHigh >= 0x30)&&(bDataHigh <= 0x39)){
			iValue = bDataHigh - 0x30;
		}else{
			iValue = bDataHigh - 'A' + 10;
		}
		iValue = iValue&0x0F;
		iValue = iValue<<4;
		if((bDataLow >= 0x30)&&(bDataLow <= 0x39)){
			iValue += bDataLow - 0x30;
		}else{
			iValue += bDataLow - 'A' + 10;
		}
		
		return (byte)iValue;
	}
	public static String bytesToHexString(byte[] src){   
	    StringBuffer stringBuilder = new StringBuffer("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < src.length; i++) {   
	        int v = src[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }   
	    return stringBuilder.toString();   
	} 
	//ʮ������ַ�תΪbyte����
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}
	private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}
}
