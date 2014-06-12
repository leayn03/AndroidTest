package com.meal.saleactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.meal.util.ErrUtil;

public class UpdateSalePassword extends BaseActivity{
	

	Button saveUpdate;
	ImageButton returnButton;

	EditText newPassword1;
	EditText newPassword2;
	
	private SellerManageAction sellerManage = SellerManageAction.getInstance(); // 初始化实例
	Seller seller = SaleGlobal.seller;
	
	private void addEventListener() {
		
		 newPassword1 = (EditText) findViewById(R.id.updateSalePassword);
		 newPassword2= (EditText) findViewById(R.id.updateConfirmSalePassword);
		 
		
		 
		addClickEventListener(R.id.SaveUploadSalePassword,new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final String passwordstring1 = newPassword1.getText().toString();
						final String passwordstring2 = newPassword2.getText().toString();
						if(isRightPasswd(passwordstring1)&&isRightPasswd(passwordstring2))
						{
							if(passwordstring1.equals(passwordstring2))
							{
								//Toast.makeText(UpdateSalePassword.this, "两次密码输入相同，可修改", Toast.LENGTH_LONG).show();    //testToast
	      						seller.setPassWord(passwordstring1);
								startAsynThread("updatesellerpassword");
							}

							else 
							{
								 Toast.makeText(UpdateSalePassword.this, "两次密码输入不同，请重新输入",
								 Toast.LENGTH_LONG).show();
							}
						}
						else
						{
							Toast.makeText(UpdateSalePassword.this, "密码输入不符合要求，请输入6-20位数字或字母！",
									 Toast.LENGTH_LONG).show();
						}
						


		
					}
				});
	}

	private void initial() {

		setUIRefreshConfig(new UIThreadImpl() {

			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub
				
				Toast.makeText(UpdateSalePassword.this, "密码修改成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(UpdateSalePassword.this, UpdateSaleInfo.class);
				startActivity(intent);
				UpdateSalePassword.this.finish();
			}

		});

		setAsynThreadConfig("updatesellerpassword", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub

				Message msg = Message.obtain();
				msg.obj = sellerManage.updateSellerInfo(seller);

				finishAsynThread("updatesellerpassword");

				return msg;
			}

		});
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.updatesalepassword);
		

		returnButton=(ImageButton)findViewById(R.id.updatePasswordReturnButton);
		
		
		//监听返回键
        returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(UpdateSalePassword.this,UpdateSaleInfo.class);
				startActivity(intent);
			}
		});
        
		initial();
		addEventListener();
	}

	private boolean isRightPasswd(String passwd)//只允许6到20位数字+字符 
	{
		boolean isRight = false;
	
		if(passwd == null || passwd.equals("") || passwd.length()<6 || passwd.length()>20)
		{
			return isRight;
		}
	
		for(int i=0 ; i<passwd.length() ; i++)
		{
				char c = passwd.charAt(i);
				if((c>='0' && c<='9') || (c>='a' && c<='z') || c>='A' && c<='Z')
			{
		
			}
			else
			{
				return isRight;
			}
		}
	
		isRight = true;
		return isRight;
	}
}
