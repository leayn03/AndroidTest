package com.meal.saleactivity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.salemealapp.R;
import com.meal.action.MenuConfigAction;
import com.meal.action.SellerManageAction;
import com.meal.action.UserManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Global;
import com.meal.bean.Seller;
import com.meal.bean.User;
import com.meal.util.DialogUtil;

public class SaleRegisterActivity extends BaseActivity {

	private MenuConfigAction menuConfig = MenuConfigAction.getInstance();

	private Seller seller = null;

	private SellerManageAction sellerManage = SellerManageAction.getInstance(); // 初始化

	EditText registerPhone;
	EditText registerName;
	EditText registerAdd;
	EditText passwordFirst;
	EditText passwordConfirm;
	Button registerButton;

	private void addEventListener() {
		registerPhone = (EditText) findViewById(R.id.registerPhone);
		registerName = (EditText) findViewById(R.id.registerName);
		registerAdd = (EditText) findViewById(R.id.registerAddress);

		passwordFirst = (EditText) findViewById(R.id.registerPassword);
		passwordConfirm = (EditText) findViewById(R.id.registerConfirm);

		addClickEventListener(R.id.registerButton, new View.OnClickListener() {

			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				initial();
				
				final String phoneNum1 = registerPhone.getText().toString();
				final String nameSale = registerName.getText().toString();
				final String address = registerAdd.getText().toString();
				final String password1 = passwordFirst.getText().toString();
				final String password2 = passwordConfirm.getText().toString();
				
				if(null != address && address.length() !=0 && null !=nameSale && nameSale.length() !=0)
				{
					 if(isMobileNO(phoneNum1))
					 {
						 if(isRightPasswd(password1)&&isRightPasswd(password2))
						 {
							 if(password1.equals(password2))
							 {
//									 Toast.makeText(SaleRegisterActivity.this, "手机号码有效，两次密码输入相同!",
//									 Toast.LENGTH_LONG).show();    //testToast
									seller = new Seller(phoneNum1, password1, null, nameSale,
											phoneNum1, address, null, 0);

									startAsynThread("register");
							 }
							 else
							 {
									 Toast.makeText(SaleRegisterActivity.this, "两次密码输入不同，请重新输入",
									 Toast.LENGTH_LONG).show();
							 }
						 }
						 
						 else
							 
						 {
							 Toast.makeText(SaleRegisterActivity.this, "密码输入不符合要求，请输入6-20位数字或字符！",
							 Toast.LENGTH_LONG).show();
						 }
						
					 }
					 else
					 {
							 Toast.makeText(SaleRegisterActivity.this, "电话号码输入有误，请重新输入",
							 Toast.LENGTH_LONG).show();
					 }
				}
				else 
				{
					 Toast.makeText(SaleRegisterActivity.this, "地址或者店名输入有误，请重新输入！",
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

				if (1 == msg.arg2) {

					//DialogUtil.alert("注册成功，请等待管理员确认", SaleRegisterActivity.this);
					Toast toast=Toast.makeText(SaleRegisterActivity.this, "注册成功！",Toast.LENGTH_LONG);//输入有误，请重新输入
			    	toast.setGravity(Gravity.TOP , 0,300);
			    	toast.show();
					
					//SaleRegisterActivity.this.finish();
					Intent intent = new Intent();
					intent.setClass(SaleRegisterActivity.this,SaleShowPageActivity.class); //注册成功跳转开始页，应该写退出程序？？？
					startActivity(intent);
					

				} else {

					DialogUtil.alert("注册失败！", SaleRegisterActivity.this);
				}

			}
		});

		setAsynThreadConfig("register", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();

				if (sellerManage.register(seller)) {

					msg.obj = Global.seller;
					msg.arg2 = 1;
				}

				finishAsynThread("register");

				return msg;
			}

		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saleregist);

//		initial();
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

	public boolean isMobileNO(String mobiles)// 判断输入电话号码有误

	{
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches() + "---");
		return m.matches();
	}

}
