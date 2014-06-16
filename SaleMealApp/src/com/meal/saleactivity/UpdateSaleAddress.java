package com.meal.saleactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.salemealapp.R;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Seller;
import com.meal.saleglobal.SaleGlobal;
import android.view.View.OnClickListener;

public class UpdateSaleAddress extends BaseActivity {

	Button SaveUploadSaleAddress;
	ImageButton updateSaleAddressReturnButton;

	private SellerManageAction sellerManage = SellerManageAction.getInstance(); // 初始化实例
	Seller seller = SaleGlobal.seller;

	private void addEventListener() {
		final EditText address = (EditText) findViewById(R.id.updateSaleAddress);

		addClickEventListener(R.id.SaveUploadSaleAddress,
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String addressstring = address.getText().toString();
						if(0 != addressstring.length())
						{
							seller.setAddress(addressstring);
							startAsynThread("updateselleraddress");
						}
						else
						{
					    	Toast toast=Toast.makeText(UpdateSaleAddress.this, "输入不能为空！",Toast.LENGTH_LONG);//输入有误，请重新输入
					    	toast.setGravity(Gravity.TOP , 0,180);
					    	toast.show();
						}


					}
				});
	}

	private void initial() {

		setUIRefreshConfig(new UIThreadImpl() {

			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				Toast.makeText(UpdateSaleAddress.this, "地址修改成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(UpdateSaleAddress.this, UpdateSaleInfo.class);
				startActivity(intent);
				UpdateSaleAddress.this.finish();
			}

		});

		setAsynThreadConfig("updateselleraddress", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub

				Message msg = Message.obtain();
				msg.obj = sellerManage.updateSellerInfo(seller);

				finishAsynThread("updateselleraddress");

				return msg;
			}

		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatesaleaddress);

	
		 updateSaleAddressReturnButton=(ImageButton)findViewById(R.id.updateSaleAddressReturnButton);
		

		 updateSaleAddressReturnButton.setOnClickListener(new OnClickListener() {
		
			 @Override
			 public void onClick(View v) {
			 // TODO Auto-generated method stub
			
			 Intent intent=new Intent();
			 intent.setClass(UpdateSaleAddress.this,UpdateSaleInfo.class);
			 startActivity(intent);
			 }
		 });


		initial();

		addEventListener();
	}


}
