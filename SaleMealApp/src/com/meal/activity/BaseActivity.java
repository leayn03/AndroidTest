package com.meal.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;

/**
 * @author xiamingxing
 * 
 */
public abstract class BaseActivity extends Activity {

	private Thread asynThread;

	private Map<String, Thread> asynThreadList;

	private Map<String, Boolean> closeAsynThreadList;

	private Handler handler;

	private UIThreadImpl ui;

	/**
	 * @param id
	 * @param asynImpl
	 */
	private void _initAsynThread(final String id, final AsynThreadImpl asynImpl) {

		asynThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Message msg = null;

				if (null != asynImpl && null != closeAsynThreadList
						&& null != closeAsynThreadList.get(id)) {

					do {

						msg = asynImpl.excute();

						if (null == msg) {

							msg = Message.obtain();
							msg.arg1 = -1;
							msg.obj = "错误！";

						}

						handler.sendMessage(msg);

					} while (!closeAsynThreadList.get(id).equals(true));

				}

				asynThreadList.put(id, null);

			}
		});

		if (asynThreadList == null) {

			asynThreadList = new HashMap<String, Thread>();

		}

		asynThreadList.put(id, asynThread);

	}

	/**
     *
     */
	private void _initUIRefresh() {

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (null != msg) {

					super.handleMessage(msg);

					if (null != ui && -1 != msg.arg1) {

						ui.refresh(msg);

					} else {

						// DialogUtil.alert((String) msg.obj,
						// BaseActivity.this);

					}
				}
			}
		};
	}

	/**
	 * @param viewId
	 * @param onClickListener
	 */
	public void addClickEventListener(int viewId,
			View.OnClickListener onClickListener) {

		View ele = findViewById(viewId);

		ele.setOnClickListener(onClickListener);

	}

	/**
	 * @param viewId
	 * @param onTouceListener
	 */
	public void addTouchEventListener(int viewId,
			View.OnTouchListener onTouceListener) {

		View ele = findViewById(viewId);

		ele.setOnTouchListener(onTouceListener);

	}

	/**
	 * @param id
	 */
	public void finishAsynThread(String id) {

		if (null != closeAsynThreadList && null != closeAsynThreadList.get(id)) {

			closeAsynThreadList.put(id, new Boolean(true));

		}

	}

	/**
	 * @param id
	 * @param isRepeat
	 * @param asynImpl
	 */
	public void setAsynThreadConfig(String id, boolean isRepeat,
			AsynThreadImpl asynImpl) {

		if (null == closeAsynThreadList) {

			closeAsynThreadList = new HashMap<String, Boolean>();

		}

		if (isRepeat) {

			closeAsynThreadList.put(id, new Boolean(false));

		} else {

			closeAsynThreadList.put(id, new Boolean(true));

		}

		_initAsynThread(id, asynImpl);

	}

	public void setUIRefreshConfig(UIThreadImpl ui) {

		this.ui = ui;

		_initUIRefresh();

	}

	/**
	 * @param id
	 */
	public void startAsynThread(String id) {

		asynThread = (Thread) asynThreadList.get(id);

		if (null != asynThread && !asynThread.isAlive()) {
			
			try {
				
				asynThread.start();
				
			}
			catch(Exception e){
				
				e.printStackTrace();
				
			}

		}

	}

}
