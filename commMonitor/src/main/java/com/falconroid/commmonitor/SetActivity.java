package com.falconroid.commmonitor;

import com.falconroid.service.Platform;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SetActivity extends Activity implements OnClickListener{
	
	private Button mBtnOpen;
	private Button mBtnCancel;
	
	private EditText mEtCommPort;
	private EditText mEtCommBaud;
	
	private EditText mEtGpioPort;
	
	private TextView mlbCommPortStatus;
	private TextView mlbCommBaudStatus;
			
	private Button mBtnHigh;
	private Button mBtnLow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		
		mBtnOpen = (Button)this.findViewById(R.id.btnOpen);
		mBtnCancel = (Button)this.findViewById(R.id.btnCancel);
		
		mBtnOpen.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
			
		mEtCommPort = (EditText)this.findViewById(R.id.etCommNum);
		mEtCommBaud = (EditText)this.findViewById(R.id.etBaud);
		
		mEtGpioPort = (EditText)this.findViewById(R.id.etGpioNum);
		mEtGpioPort.setText("70");
		
		mBtnHigh = (Button)this.findViewById(R.id.btnGpioHigh);
		mBtnLow = (Button)this.findViewById(R.id.btnGpioLow);
		
		mBtnHigh.setOnClickListener(this);
		mBtnLow.setOnClickListener(this);
		
		mEtCommPort.setText("1");
		mEtCommBaud.setText("115200");
		
		mlbCommPortStatus = (TextView)this.findViewById(R.id.lbCommNumStatus);
		mlbCommBaudStatus = (TextView)this.findViewById(R.id.lbCommBaudStatus);
	}	
	
	public void onClick(View view){
		if(mBtnOpen.equals(view)){
			if((mEtCommPort.length() == 0) || (mEtCommBaud.length() == 0)){
				return;
			}
			
			int iPort,iBaud;
			try{
				iPort = Integer.parseInt(mEtCommPort.getText().toString());
				
				mlbCommPortStatus.setText("format succ");
			}catch(java.lang.NumberFormatException nfe){
				mlbCommPortStatus.setText("format error");
				return;
			}
			
			try{
				iBaud = Integer.parseInt(mEtCommBaud.getText().toString());
				
				mlbCommBaudStatus.setText("format succ");
			}catch(java.lang.NumberFormatException nfe){
				mlbCommBaudStatus.setText("format error");
				return;
			}			
			
			//put it
			Intent intent = new Intent(this,CommActivity.class);
			Bundle bundle = new Bundle();
			
			bundle.putInt(CParamKey.KEY_COMM_PORT, iPort);
			bundle.putInt(CParamKey.KEY_COMM_BAUD, iBaud);
			
			intent.putExtras(bundle);
			
			this.setResult(RESULT_OK, intent);
			
			this.finish();
		}else if(mBtnCancel.equals(view)){
			this.setResult(RESULT_CANCELED);
			this.finish();
		}else if(mBtnHigh.equals(view)){
			try{
				int iGpioNum = Integer.parseInt(mEtGpioPort.getText().toString());
				
				Platform.SetGpioOutput(iGpioNum);
				Platform.SetGpioDataHigh(iGpioNum);
				
				mlbCommBaudStatus.setText("set succ:"+iGpioNum);
			}catch(java.lang.NumberFormatException nfe){
				mlbCommBaudStatus.setText("format error");
				return;
			}	
		}else if(mBtnLow.equals(view)){
			try{
				int iGpioNum = Integer.parseInt(mEtGpioPort.getText().toString());
				
				Platform.SetGpioOutput(iGpioNum);
				Platform.SetGpioDataLow(iGpioNum);
				
				mlbCommBaudStatus.setText("set succ:"+iGpioNum);
			}catch(java.lang.NumberFormatException nfe){
				mlbCommBaudStatus.setText("format error");
				return;
			}	
		}
	}
	
	
}
