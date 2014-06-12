package com.meal.saleactivity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.meal.dialog.MyProgressDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salemealapp.R;
import com.meal.action.MenuConfigAction;
import com.meal.action.SellerManageAction;
import com.meal.activity.BaseActivity;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Seller;
import com.meal.saleglobal.SaleGlobal;
import com.meal.util.DialogUtil;
import com.meal.util.ErrUtil;
import com.meal.util.SysUtil;

public class SaleLoginActivity extends BaseActivity{
	
	private MyProgressDialog loginProgressDialog;
	

	
	private HashMap<String, String> loginInfo = new HashMap<String, String>(); //loginInfo

	private MenuConfigAction menuConfig = MenuConfigAction.getInstance();

	private Seller seller = null;

	private SellerManageAction sellerManage = SellerManageAction.getInstance(); //初始化
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salelogin);
		
	    TextView textView=(TextView)findViewById(R.id.registerGo);		//注册跳转
	   
	    textView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );//底部加横线
	    textView.setTypeface(Typeface.MONOSPACE,Typeface.ITALIC);//斜体，中文有效 
	    textView.getPaint().setAntiAlias(true);//抗锯齿

	
		//监听无账号注册链接
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//跳转到注册页面
				
				Intent intent=new Intent();
				intent.setClass(SaleLoginActivity.this,SaleRegisterActivity.class);
				startActivity(intent);
				
			}
		});
		
		loginProgressDialog = MyProgressDialog.createDialog(this);
//		initial();
		addEventListener();
		
	}
	
	private void addEventListener(){
		
		final EditText loginPhone=(EditText)findViewById(R.id.loginPhone); //登陆页面电话号码
	    final EditText loginPasswd=(EditText)findViewById(R.id.loginPasswd); //登陆页面密码
	    
	    addClickEventListener(R.id.loginButton, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
		       initial();
				String sellerName=loginPhone.getText().toString(); //获取输入的电话号码,作为sellerName
			    String passWord=loginPasswd.getText().toString(); //获取输入的密码
			    
			    if(isMobileNO(sellerName))
			    {
			    	if(isRightPasswd(passWord))
			    	{
			    		 loginProgressDialog.show();
			    		 
			    		 loginInfo.clear();
			    		 loginInfo.put("sellerName", sellerName);
						 loginInfo.put("passWord", passWord);
						 
						 startAsynThread("login");
			    	}
			    	else 
			    	{
			    		Toast toast=Toast.makeText(SaleLoginActivity.this, "密码输入有误，请输入6-20位数字或字符！",Toast.LENGTH_LONG);//输入有误，请重新输入
				    	toast.setGravity(Gravity.TOP , 0,180);
				    	toast.show();
			    	}
			    	   
	            }
			    else
			    {
			    	Toast toast=Toast.makeText(SaleLoginActivity.this, "电话输入有误，请重新输入",Toast.LENGTH_LONG);//输入有误，请重新输入
			    	toast.setGravity(Gravity.TOP , 0,180);
			    	toast.show();
			    }
			  
			}
		});
		
	}
	
private void initial()
{
	setUIRefreshConfig(new UIThreadImpl() {
		
		@Override
		public void refresh(Message msg) {
			// TODO Auto-generated method stub
			loginProgressDialog.dismiss();
			if ( 1 == msg.arg2 ){
				
				SaleGlobal.seller = (Seller) msg.obj;//  设置
				//认证成功跳转
				loginStart();
			}
			else{

				if(SysUtil.getAPNType(getApplicationContext()) == -1)
				{
					DialogUtil.alert("网络异常,请检查网络！", SaleLoginActivity.this);
				}
				else
				{
					DialogUtil.alert("登录失败！", SaleLoginActivity.this);
				}
				
				
			}
			
		}
	});
	
	setAsynThreadConfig("login", true, new AsynThreadImpl() {

		@Override
		public Message excute() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			
			msg.arg1 = 1;
			msg.obj = sellerManage.login(loginInfo.get("sellerName"),
					loginInfo.get("passWord"));
			if ( null != msg.obj ){
				msg.arg2 = 1;
			}
			else{
				msg.arg2 = 0;
			}

			finishAsynThread("login");

			return msg;
		}

	});
	
}
	
	

	
	
	
	//覆写onKeyDown函数，监听返回键
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
	
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//	    if(keyCode == KeyEvent.KEYCODE_BACK)  
//	       {    
//	           exitBy2Click();      //调用双击退出函数  
//	       }  
//	    return false; 
//	}

	public void loginStart() // 登陆成功跳转
	{
    	Toast.makeText(SaleLoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
    	
		//跳转到商户信息页面
		Intent intent=new Intent();
		intent.setClass(SaleLoginActivity.this,SaleInfoActivity.class);
		startActivity(intent);
		
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
   public boolean isMobileNO(String mobiles)//判断输入电话号码有误

	{  
	   Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
       Matcher m = p.matcher(mobiles);   
	   System.out.println(m.matches()+"---");    
        return m.matches();
      }
	
//	/**
//	 * 双击退出函数
//	 */
//	private static Boolean isExit = false;
//
//	private void exitBy2Click() {
//		Timer tExit = null;
//		if (isExit == false) {
//			isExit = true; // 准备退出
//			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//			tExit = new Timer();
//			tExit.schedule(new TimerTask() {
//				@Override
//				public void run() {
//					isExit = false; // 取消退出
//				}
//			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
//
//		} else {
//			finish();
//			System.exit(0);
//		}
//	}
	

}
