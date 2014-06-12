package com.meal.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.example.salemealapp.R;
import com.meal.action.MenuConfigAction;
import com.meal.action.UserManageAction;
import com.meal.activity.ipml.AsynThreadImpl;
import com.meal.activity.ipml.UIThreadImpl;
import com.meal.bean.Global;
import com.meal.bean.User;
import com.meal.util.BeanUtil;
import com.meal.util.DialogUtil;
import com.meal.util.SysUtil;

/**
 * @author xiamingxing
 * 
 */
public class TestActivity extends BaseActivity {

	private HashMap<String, String> loginInfo = new HashMap<String, String>();

	private MenuConfigAction menuConfig = MenuConfigAction.getInstance();

	private User user = null;

	private UserManageAction userManage = UserManageAction.getInstance();

	private void addEventListener() {

		final EditText edt_passWord = (EditText) findViewById(R.id.editText_passWord);

		final EditText edt_userName = (EditText) findViewById(R.id.editText_userName);

		addClickEventListener(R.id.btn_submit, new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String userName = edt_userName.getText().toString();

				String passWord = edt_passWord.getText().toString();

				loginInfo.put("userName", "xiamingxing");

				loginInfo.put("passWord", "123456");

				startAsynThread("login");

			}
		});

		addClickEventListener(R.id.btn_register, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				user = new User("xiamingxing", "123456", null, "18010050656",
						"北京市");

				startAsynThread("register");

			}
		});

		addClickEventListener(R.id.btn_test, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startAsynThread("test");

			}
		});
	}

	private void initial() {
		// TODO Auto-generated constructor stub
		setUIRefreshConfig(new UIThreadImpl() {
			@Override
			public void refresh(Message msg) {
				// TODO Auto-generated method stub

				String content = "";

				// {
				// ImageView imageView = (ImageView)
				// findViewById(R.id.image_test);
				// imageView.setImageBitmap((Bitmap) msg.obj);
				// }
				//
				// {
				// User user = (User) msg.obj;
				// String content = JSONFactoryUtil.parserToJsonString(user);

				// }

				// {
				//
				// content = msg.obj.toString();
				// }

				{
					// content = JSONFactoryUtil.parserToJsonString(msg.obj);

				}

				DialogUtil.alert(content, TestActivity.this);
			}
		});

		setAsynThreadConfig("login", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();

				msg.obj = userManage.login(loginInfo.get("userName"),
						loginInfo.get("passWord"));

				finishAsynThread("login");

				return msg;
			}

		});

		setAsynThreadConfig("register", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();

				if (userManage.register(user)) {

					msg.obj = Global.user;
				}

				finishAsynThread("register");

				return msg;
			}

		});

		setAsynThreadConfig("test", true, new AsynThreadImpl() {

			@Override
			public Message excute() {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				// Global.seller = new Seller();
				Global.user = new User("sunxiaojun", "123456", null,
						"18010050656", "北京市");

				// {
				// userManage.updateUserInfo(new User("sunxiaojun", "123456",
				// null, "18010050656", "北京市"));
				// msg.obj = Global.user;
				// }

//				{
//					String url = "http://tb.himg.baidu.com/sys/portrait/item/25385f0a";
//
//					Bitmap bmp = userManage.getUserPortrait(url);
//
//					msg.obj = bmp;
//				}

				// {
				// try {
				// Global.user.setUid(1l);
				// File file = new File("/mnt/sdcard/","1.3gp");
				// String URL = HttpUtil.Upload(Constant.USER_MODULE, "uid="
				// + Global.user.getUid(), file);
				// msg.obj = URL;
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// }

//				{
//					File f = FileUtils.creatSDDir("test");
//
//				}

				// {
				//
				// Global.seller = new Seller();
				// msg.obj = menuConfig.addMenu(new Menu("糖醋里脊", null, 100l,
				// 0.8));
				//
				// }

				// {
				//
				// msg.obj = menuConfig.getMenuDetail(1l);
				// }

				// {
				//
				// menuConfig.updateMenu(1l, new Menu("糖醋里脊", null, 100l, 0.8));
				//
				// }

				// {
				// msg.obj = menuConfig.getMenuList(1l);
				//
				// }

				// {
				// menuConfig.deleteMenu(1l);
				//
				// }

				// {
				// menuConfig.clearMenuListBySid(1l);
				//
				// }

				{
						int x = SysUtil.getAPNType(getApplicationContext());
						System.out.print(x);
						User user = new User("123", "123", "123", "123", "123");
						try {
							
							User newUser = (User) BeanUtil.cloneObject(user);
							
							newUser.setName("xiamingxing");
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						String name = user.getName();
						System.out.print(name);
				}

				finishAsynThread("test");
				return msg;
			}

		});

	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		initial();

		addEventListener();

	}
}
