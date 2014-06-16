package com.meal.saleactivity;

import java.util.Timer;
import java.util.TimerTask;

import com.example.salemealapp.R;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Global;
import com.meal.bean.Seller;
import com.meal.dialog.MyProgressDialog;
import com.meal.saleglobal.SaleGlobal;
import com.meal.util.DialogUtil;
import com.meal.util.SysUtil;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SaleInfoActivity extends BaseActivity{

	Button acceptButton;
	Button closeButton;
	Button orderListButton;
	ImageButton mySetting;
	Boolean switchState;
	Bitmap bitmap;
	ImageView imageSale;
	

    private SellerManageAction sellerManage=SellerManageAction.getInstance();  //	初始化实例
    
	private MyProgressDialog loginProgressDialog;   //progressbar 1
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.saleinfo);
		
		//获取商户的图片
		imageSale=(ImageView)findViewById(R.id.saleImage);		
		//获取商户信息，名称和ID，再从数据库中获取并显示
		TextView textView1=(TextView)findViewById(R.id.salesInfo);
		//获取商户信息，地址和电话，再从数据库中获取并显示
		TextView textView2=(TextView)findViewById(R.id.salesAddress);
		//读取显示
		loginProgressDialog = MyProgressDialog.createDialog(this);   //progressbar 2
		pictureEvent();
		if(SaleGlobal.seller!=null)
		{
			switchState=SaleGlobal.seller.getOrderFunctionSwitch();//获取商户接单状态
		}
		if(null==switchState)
		{
			switchState=false;
		}

		
		String name = null, address = null, phone =null ;
	
	    if(SaleGlobal.seller!=null)
	    {
	    	loginProgressDialog.show();           //progressbar3
	    	name=SaleGlobal.seller.getDescription();
	    	address=SaleGlobal.seller.getAddress();
	        phone=SaleGlobal.seller.getPhone();
	        
	        
	        imageSale.setImageBitmap(bitmap);
	        
	        textView1.setText(name);
	        textView2.setText(address+"  "+phone);
	        
	        loginProgressDialog.dismiss();  //progressbar 4
	  		
	    }
	    else 
	    {
	    	Intent intent = new Intent();
	        intent.setClass(SaleInfoActivity.this, SaleLoginActivity.class);
	        startActivity(intent);
	    }
	
		 mySetting=(ImageButton)findViewById(R.id.mySetting);
		 acceptButton=(Button)findViewById(R.id.acceptButton);
		 closeButton=(Button)findViewById(R.id.closeButton);
		 orderListButton=(Button)findViewById(R.id.orderListButton);
		 

		 
		 
		 //个人设置
		 mySetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//跳转设置页面
				Intent intent=new Intent();
				intent.setClass(SaleInfoActivity.this,UpdateSaleInfo.class);
				startActivity(intent);
			
			}
		});
		
		
		//上传商品接口
		Button uploadButton=(Button)findViewById(R.id.uploadButton);
		uploadButton.setOnClickListener(new OnClickListener() {
						
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(SaleInfoActivity.this,UploadNewGood.class);
				startActivity(intent);
				
			}
		});
		
		
		//修改商品接口
		Button changeButton=(Button)findViewById(R.id.changeButton);
		changeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(SaleInfoActivity.this, "亲，这个功能下个版本完成，求放过！！！", Toast.LENGTH_LONG).show();
				
				
			}
		});
		
		
		
		if(false == switchState)
		{
			acceptButton.setVisibility(View.VISIBLE);
			closeButton.setVisibility(View.INVISIBLE);
			orderListButton.setVisibility(View.INVISIBLE);
		}
		else 
		{
			acceptButton.setVisibility(View.INVISIBLE);
			closeButton.setVisibility(View.VISIBLE);
			orderListButton.setVisibility(View.VISIBLE);
		}
		
		
		 openEventListener();//商家接单打开，新起线程修改服务期状态
		 closeEventListener(); //关闭商家接单开关，新起线程修改服务期状态

	    //按查看订单列表按钮跳转
	    orderListButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//跳转到订单信息页面
				Intent intent=new Intent();
				intent.setClass(SaleInfoActivity.this,SaleOrderList.class);
				startActivity(intent);
				
			}
		});
	    
		
		
	}

	private void pictureEvent(){
		     loginProgressDialog.show();           //progressbar3
		     initialPicture();
		     startAsynThread("getSalePicture");
	}
	
	
	private void initialPicture()
	{
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				imageSale.setImageBitmap(bitmap);
			}
		});
		
		setAsynThreadConfig("getSalePicture", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				

                String saleLogo = SaleGlobal.seller.getLogo();
				bitmap = sellerManage.getLogo(saleLogo);

				finishAsynThread("getSalePicture");

				return msg;
			}

		});
		
	}	
	
	
	private void openEventListener(){
		   
	    addClickEventListener(R.id.acceptButton, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				acceptButton.setVisibility(View.INVISIBLE);
				closeButton.setVisibility(View.VISIBLE);
				orderListButton.setVisibility(View.VISIBLE);
				initialOpen();
				 startAsynThread("openOrderState");
			}
		});	
	}
	
	
	private void initialOpen()
	{
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub

			}
		});
		
		setAsynThreadConfig("openOrderState", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				


				sellerManage.openOrderFuntion(SaleGlobal.seller.getSid());

				finishAsynThread("openOrderState");

				return msg;
			}

		});
		
	}	
	
	
	private void closeEventListener(){
		   
	    addClickEventListener(R.id.closeButton, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				acceptButton.setVisibility(View.VISIBLE);
				closeButton.setVisibility(View.INVISIBLE);
				orderListButton.setVisibility(View.INVISIBLE);
				
				initialClose();
				 startAsynThread("closeOrderState");
			}
		});	
	}
	
	private void initialClose()
	{
		setUIRefreshConfig(new UIThreadImpl() {
			
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub

			}
		});
		
		setAsynThreadConfig("closeOrderState", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				


				sellerManage.closeOrderFuntion(SaleGlobal.seller.getSid());

				finishAsynThread("closeOrderState");

				return msg;
			}

		});
		
	}	
	
	//覆写onKeyDown函数，监听返回键  双击退出到登陆页面
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		PackageManager pm = getPackageManager();
		ResolveInfo homeInfo =
		pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		ActivityInfo ai = homeInfo.activityInfo;
		Intent startIntent = new Intent(Intent.ACTION_MAIN);
		startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
		startActivitySafely(startIntent);
		return true;
		
		} else
				return super.onKeyDown(keyCode, event);
		}
		private void startActivitySafely(Intent intent) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, "null",
				Toast.LENGTH_SHORT).show();
			} catch (SecurityException e) {
				Toast.makeText(this, "null",
				Toast.LENGTH_SHORT).show();
		}
} 	
	

}
