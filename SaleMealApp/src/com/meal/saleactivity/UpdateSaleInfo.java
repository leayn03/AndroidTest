package com.meal.saleactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.salemealapp.R;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.saleglobal.SaleGlobal;


public class UpdateSaleInfo extends BaseActivity{
	
	Button returnHomeButton;
	Button updatePhoneButton;
	Button updateSaleAddressButton;
	Button updateSalePasswordButton;
	ImageButton updateSalePictureNextButton;
	ImageButton updateSaleInfoReturnButton;
	ImageView salePicture;
	
	String logo=null;
	Bitmap bitmapPicture=null;

	

    private SellerManageAction sellerManage=SellerManageAction.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.updatesaleinfo);
		
		salePicture=(ImageView)findViewById(R.id.salePicture);
		returnHomeButton = (Button)findViewById(R.id.logOutSale);
		updatePhoneButton=(Button)findViewById(R.id.updatePhoneButton);
		updateSaleAddressButton=(Button)findViewById(R.id.updateSaleAddressButton);
		updateSalePasswordButton=(Button)findViewById(R.id.updateSalePasswordButton);
		updateSalePictureNextButton=(ImageButton)findViewById(R.id.updateSalePictureNext);
		updateSaleInfoReturnButton=(ImageButton)findViewById(R.id.updateSaleInfoReturnButton);
		
		
		String name = null, address = null, phone =null ;
	
	    if(SaleGlobal.seller!=null)
	    {
	    	name=SaleGlobal.seller.getName();
	    	address=SaleGlobal.seller.getAddress();
	        phone=SaleGlobal.seller.getPhone();

	    }
	    
	    salePicture.setImageBitmap(bitmapPicture);
	    
	    showPictureEventListener(); //显示商家端图
		closeEventListener();//退出登录同时把状态设置为false
		
		//监听修改商户照片
		updateSalePictureNextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent();
				intent.setClass(UpdateSaleInfo.this,UpdateSalePicture.class);
				startActivity(intent);
				UpdateSaleInfo.this.finish();
			}
		});
		
		
		updatePhoneButton.setText("手机号码       "+phone);
		
		updateSaleAddressButton.setText("商户地址       "+address);
		

		
		//监听手机号码修改按键
		updatePhoneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent();
				intent.setClass(UpdateSaleInfo.this,UpdateSalePhone.class);
				startActivity(intent);
				UpdateSaleInfo.this.finish();
				
			}
		});
		
		//监听地址更改Button
		updateSaleAddressButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent();
				intent.setClass(UpdateSaleInfo.this,UpdateSaleAddress.class);
				startActivity(intent);
				UpdateSaleInfo.this.finish();
			}
		});
		
		//监听密码更改Button
		updateSalePasswordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent();
				intent.setClass(UpdateSaleInfo.this,UpdateSalePassword.class);
				startActivity(intent);
				UpdateSaleInfo.this.finish();
			}
		});
		

		
		//监听返回键
		updateSaleInfoReturnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				Intent intent=new Intent();
				intent.setClass(UpdateSaleInfo.this,SaleInfoActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	private void showPictureEventListener(){
		   
				 initial();
				 startAsynThread("showpicture");

	}
	   private void initial()
	   {
		   
		   setUIRefreshConfig(new UIThreadImpl()
		   {

			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				
					salePicture.setImageBitmap(bitmapPicture);


			}
			   
		   });
		   setAsynThreadConfig("showpicture",true, new AsynThreadImpl()
		   {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				
				Message msg=Message.obtain();
		        logo=SaleGlobal.seller.getLogo(); 
		        
		        bitmapPicture=sellerManage.getLogo(logo);
		        
				finishAsynThread("showpicture");
				
				return msg;
		    	}
			   
		   });
	   }

private void closeEventListener(){
			   
		    addClickEventListener(R.id.logOutSale, new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					 initialClose();
					 startAsynThread("logoutcloseOrderState");
				}
			});	
		}
		
		private void initialClose()
		{
			setUIRefreshConfig(new UIThreadImpl() {
				
				@Override
				public void refresh(Message msg) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(UpdateSaleInfo.this,SaleLoginActivity.class);
					startActivity(intent);
				}
			});
			
			setAsynThreadConfig("logoutcloseOrderState", true, new AsynThreadImpl() {

				@Override
				public Message excute() {
					// TODO Auto-generated method stub
					Message msg = Message.obtain();
					


					sellerManage.closeOrderFuntion(SaleGlobal.seller.getSid());

					finishAsynThread("logoutcloseOrderState");

					return msg;
				}

			});
			
		}	
	   
}
