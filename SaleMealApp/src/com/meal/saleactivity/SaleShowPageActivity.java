package com.meal.saleactivity;


import com.example.salemealapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SaleShowPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpage);
		new Handler().postDelayed(new Runnable() {
			public void run() {
					Intent intent = new Intent();
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					intent.setClass(SaleShowPageActivity.this,SaleLoginActivity.class);
					startActivity(intent);
//				}
			}
		}, 500);
	}
	
}
