package com.falconroid.commmonitor;

import com.falconroid.comm.CommPort;
import com.falconroid.comm.ICommListener;
import com.falconroid.utils.FalconException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ListActivity;

import android.widget.Button;
import android.widget.EditText;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.content.Intent;

import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import android.view.ViewGroup;

import android.content.Context;

import android.view.LayoutInflater;

import android.widget.Toast;

import android.widget.CheckBox;

public class CommActivity extends ListActivity implements OnClickListener {
	private int miBaud;
	private int miPort;

	private Button mBtnSend;
	private Button mBtnClean;
	private Button mBtnSet;

	private EditText mEtSend;

	private CheckBox mCbHex;

	public static final int REQ_CODE_COMM_SET = 1;

	private MyBaseAdapter mAdapter;

	private class MsgHold {
		public String mType;
		public String mContent;
	}

	private ArrayList<MsgHold> mList;

	public static final String TAG = "COMM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comm);

		mBtnSend = (Button) this.findViewById(R.id.btnSend);
		mBtnClean = (Button) this.findViewById(R.id.btnClean);
		mBtnSet = (Button) this.findViewById(R.id.btnSet);

		mBtnSend.setOnClickListener(this);
		mBtnClean.setOnClickListener(this);
		mBtnSet.setOnClickListener(this);

		mEtSend = (EditText) this.findViewById(R.id.etSend);

		mEtSend.setText("AAAAAA96690003200122"); //AABB0600000001115242   AABB0600000008014148
		

		mCbHex = (CheckBox) this.findViewById(R.id.cbHex);

		CommPort.getInstance().addListener(mListener);

		mList = new ArrayList<MsgHold>();

		mAdapter = new MyBaseAdapter(this);

		this.getListView().setAdapter(mAdapter);
	}

	public void showInfo(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private class MyBaseAdapter extends BaseAdapter {
		private Context mCtx;

		public MyBaseAdapter(Context ctx) {
			mCtx = ctx;
		}

		public void addItem(String type, String msg) {
			MsgHold hold = new MsgHold();
			hold.mType = type;
			hold.mContent = msg;

			mList.add(hold);

			this.notifyDataSetChanged();
		}

		public void clearItem() {
			mList.clear();

			this.notifyDataSetChanged();
		}

		public long getItemId(int position) {
			return position;
		}

		public Object getItem(int position) {
			return position;
		}

		public int getCount() {
			return mList.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mCtx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View view = inflater.inflate(R.layout.item_layout, null);

				TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
				TextView tvData = (TextView) view.findViewById(R.id.tvData);

				MsgHold msgHold = mList.get(position);

				tvLabel.setText(msgHold.mType);
				tvData.setText(msgHold.mContent);

				return view;
			} else {
				MsgHold msgHold = mList.get(position);

				TextView tvLabel = (TextView) convertView
						.findViewById(R.id.tvLabel);
				TextView tvData = (TextView) convertView
						.findViewById(R.id.tvData);

				tvLabel.setText(msgHold.mType);
				tvData.setText(msgHold.mContent);

				return convertView;
			}
		}
	}

	public void onClick(View view) {
		if (mBtnSend.equals(view)) {
			// get data
			String strSend = mEtSend.getText().toString();

			if (strSend.length() == 0) {
				return;
			}

			boolean bChecked = mCbHex.isChecked();

			if (bChecked) {
				mAdapter.addItem("sender hex:", strSend);
			} else {
				mAdapter.addItem("sender:", strSend);
			}

			byte[] bData = new byte[strSend.length() >> 1];

			try {
				for (int i = 0; i < (bData.length); i++) {
					bData[i] = (byte) (Integer.parseInt(
							strSend.substring(i << 1, (i + 1) << 1), 16));

					Log.d(TAG,
							"bData[" + i + "]:" + Integer.toHexString(bData[i]));
				}
			} catch (java.lang.NumberFormatException nfe) {
				// Log.d(TAG, "nfe:"+nfe.getMessage());
				return;
			}

			// send it
			try {
				if(bChecked){
					CommPort.getInstance().doActionSend(bData);
				}else{
					CommPort.getInstance().doActionSend(strSend.getBytes());
				}
			} catch (FalconException fae) {
				Log.d(TAG, "FalconException:" + fae.getMessage());
			} catch (java.io.IOException ioe) {
				Log.d(TAG, "IOException:" + ioe.getMessage());
			}
		} else if (mBtnClean.equals(view)) {
			// clean list view
			mAdapter.clearItem();
		} else if (mBtnSet.equals(view)) {
			Intent intent = new Intent(this, SetActivity.class);
			this.startActivityForResult(intent, REQ_CODE_COMM_SET);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CODE_COMM_SET) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Bundle bundle = data.getExtras();

					if (bundle == null) {
						return;
					}

					miPort = bundle.getInt(CParamKey.KEY_COMM_PORT);
					miBaud = bundle.getInt(CParamKey.KEY_COMM_BAUD);

					// open comm
					new OpenT().start();
				}
			} else if (resultCode == RESULT_CANCELED) {
			}
		}
	}

	private ICommListener mListener = new ICommListener() {
		public void onCommDataIn(byte[] bData, int iStart, int iLength) {
			Log.d(TAG, "onDataComing");
			StringBuffer sb = new StringBuffer();

			for(int i=0; i<iLength; i++){
				sb.append(Integer.toHexString(bData[iStart+i]&0xFF));
				sb.append(" ");
			}
			
			Message msg = new Message();
    		Bundle bundle = new Bundle();
    		bundle.putString("recv", sb.toString());
    		msg.setData(bundle);
    		
    		mHandler.sendMessage(msg);
		}

		public void onExceptionHappen(String msg) {
			Log.d(TAG, "onExceptionHappen");
		}
	};

	private class OpenT extends Thread {
		public void run() {
			try {
				CommPort.getInstance().doActionOpen(miPort, miBaud);
			} catch (FalconException fae) {

			}
		}
	}

	public static final int MESSAGE_ID_UPDATE_RECV = 1;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();

			if (bundle != null) {
				mAdapter.addItem("recv ", bundle.getString("recv"));
			}
		}
	};

	///////////////////////////////////////////////////////////////////////////////////////////add by wen
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CommPort.getInstance().doActionClose();
	}
	
	

	/*
	 * private static final String KEY_INFO = "KEY_INFO";
	 * 
	 * private void showRecv(String info){ Message msg = new Message(); msg.what
	 * = MESSAGE_ID_UPDATE_RECV; Bundle bundle = new Bundle();
	 * bundle.putString(KEY_INFO, info); msg.setData(bundle);
	 * 
	 * mHandler.sendMessage(msg); }
	 * 
	 * private void showDebug(String info){ Message msg = new Message();
	 * 
	 * msg.what = MESSAGE_ID_UPDATE_DEBUG;
	 * 
	 * Bundle bundle = new Bundle(); bundle.putString(KEY_INFO, info);
	 * 
	 * msg.setData(bundle);
	 * 
	 * mHandler.sendMessage(msg); }
	 */
}
