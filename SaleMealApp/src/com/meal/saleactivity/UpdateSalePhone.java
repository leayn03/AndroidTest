package com.meal.saleactivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
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


public class UpdateSalePhone extends BaseActivity {
	
	Button SaveUploadSalePhone;
	ImageButton updateSalePhoneReturnButton;
    private SellerManageAction sellerManage=SellerManageAction.getInstance();  //	初始化实例
    Seller seller=SaleGlobal.seller; 
    
   private void addEventListener()
   {
	   final EditText phoneNum=(EditText)findViewById(R.id.updateSalePhone);
	   
	   addClickEventListener(R.id.SaveUploadSalePhone,new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String phone=phoneNum.getText().toString();
			if(isMobileNO(phone))
			{
				seller.setPhone(phone);
				seller.setName(phone);
				startAsynThread("updatesellerphone");
			}
			else 
			{
				Toast toast=Toast.makeText(UpdateSalePhone.this, "电话输入有误，请重新输入",Toast.LENGTH_LONG);//输入有误，请重新输入
		    	toast.setGravity(Gravity.TOP , 0,180);
		    	toast.show();
			}
			
			
		}
	});
   }
   private void initial()
   {
	   
	   setUIRefreshConfig(new UIThreadImpl()
	   {

		@Override
		public void refresh(Message msg) {
			// TODO Auto-generated method stub
			
			Toast.makeText(UpdateSalePhone.this, "电话号码修改成功", Toast.LENGTH_LONG).show();
			Intent intent=new Intent();
			intent.setClass(UpdateSalePhone.this,UpdateSaleInfo.class);
			startActivity(intent);
		}
		   
	   });
	   setAsynThreadConfig("updatesellerphone",true, new AsynThreadImpl()
	   {

		@Override
		public Message excute() {
			// TODO Auto-generated method stub
			
			Message msg=Message.obtain();
			msg.obj=sellerManage.updateSellerInfo(seller);
			
			finishAsynThread("updatesellerphone");
			
			return msg;
	    	}
		   
	   });
   }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatesalephone);
		

		updateSalePhoneReturnButton=(ImageButton)findViewById(R.id.updateSalePhoneReturnButton);
		

            updateSalePhoneReturnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent();
				intent.setClass(UpdateSalePhone.this,UpdateSaleInfo.class);
				startActivity(intent);
				
			}
		});
	
		initial();
		addEventListener();
	}

	
	   public boolean isMobileNO(String mobiles)//判断输入电话号码有误

		{  
		   Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	       Matcher m = p.matcher(mobiles);   
		   System.out.println(m.matches()+"---");    
	        return m.matches();
	      }
}
