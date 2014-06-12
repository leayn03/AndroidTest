package com.meal.action;

import java.io.File;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.meal.bean.Constant;
import com.meal.bean.Global;
import com.meal.bean.LoginInfoForUser;
import com.meal.bean.User;
import com.meal.util.HttpUtil;

/**
 * @author xiamingxing
 * 
 */
public class UserManageAction extends BaseAction {

	private static UserManageAction instance = null;

	public static UserManageAction getInstance() {

		if (null == instance) {

			instance = new UserManageAction();

		}

		return instance;

	}

	private String USER_MODULE;
	private String USER_MODULE_GET_UID_BY_USERNAME;
	private String USER_MODULE_GET_USER_INFO_BY_UID;

	private String USER_MODULE_LOGIN;
	private String USER_MODULE_LOGOUT;

	private String USER_MODULE_REGISTER;

	private String USER_MODULE_UPDATE_USER_INFO;

	private UserManageAction() {

		this.USER_MODULE = Constant.USER_MODULE;

		this.USER_MODULE_LOGIN = Constant.USER_MODULE_LOGIN;
		this.USER_MODULE_LOGOUT = Constant.USER_MODULE_LOGOUT;
		this.USER_MODULE_REGISTER = Constant.USER_MODULE_REGISTER;

		this.USER_MODULE_GET_USER_INFO_BY_UID = Constant.USER_MODULE_GET_USER_INFO_BY_UID;
		this.USER_MODULE_GET_UID_BY_USERNAME = Constant.USER_MODULE_GET_UID_BY_USERNAME;

		this.USER_MODULE_UPDATE_USER_INFO = Constant.USER_MODULE_UPDATE_USER_INFO;

	}

	/**
	 * @return
	 */
	public User freeLogin() {

		User user = null;

		if (null != Global.user) {

			user = login(Global.user.getName(), Global.user.getPassWord());
			Global.user = user;

		}

		return user;

	}

	/**
	 * @param userName
	 * @return
	 */
	public Long getUidByUserName(final String userName) {

		Long uid = null;

		if (null != userName) {

			JSONObject jsonData = (JSONObject) load(USER_MODULE,
					USER_MODULE_GET_UID_BY_USERNAME, null,
					new HashMap<String, String>() {
						{
							put("userName", userName);
						}
					});
			if (null != jsonData) {

				try {

					uid = jsonData.getLong("uid");

				} catch (JSONException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}

		}

		return uid;

	}

	/**
	 * @param uid
	 * @return
	 */
	public User getUserInfoByUid(final Long uid) {

		User user = null;

		if (null != uid) {

			user = (User) load(USER_MODULE, USER_MODULE_GET_USER_INFO_BY_UID,
					User.class, new HashMap<String, String>() {
						{
							put("uid", String.valueOf(uid));
						}
					});

		}

		return user;

	}

	/**
	 * @param uid
	 * @return
	 */
	public User getUserInfoByUserName(String userName) {

		final Long uid = getUidByUserName(userName);
		User user = null;

		if (null != uid) {

			user = (User) load(USER_MODULE, USER_MODULE_GET_USER_INFO_BY_UID,
					User.class, new HashMap<String, String>() {
						{
							put("uid", String.valueOf(uid));
						}
					});

		}

		return user;

	}

	/**
	 * @param URL
	 * @return
	 */
	public Bitmap getUserPortrait(String URL) {

		Bitmap bmp = null;

		if (null != URL && !URL.equals("")) {

			bmp = loadImage(URL);

		}

		return bmp;

	}

	/**
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public User login(final String userName, final String passWord) {

		User user = null;

		if (null != userName && null != passWord) {

			// user = (User) load(USER_MODULE, USER_MODULE_LOGIN, User.class,
			// new HashMap<String, String>() {
			// {
			//
			// put("userName", userName);
			// put("passWord", passWord);
			//
			// }
			// });
			LoginInfoForUser loginInfoForUser = new LoginInfoForUser(userName,
					passWord);

			user = (User) send(USER_MODULE, USER_MODULE_LOGIN,
					loginInfoForUser, User.class, null);

			Global.user = user;

		}

		return user;

	}

	/**
     *
     */
	public void logout() {

		final String uid = String.valueOf(Global.user.getUid());

		load(USER_MODULE, USER_MODULE_LOGOUT, User.class,
				new HashMap<String, String>() {
					{

						put("uid", uid);

					}
				});
		Global.user = null;

	}

	/**
	 * @param user
	 * @return
	 */
	public boolean register(User user) {

		boolean result = false;

		if (null != user) {

			JSONObject jsonData = (JSONObject) send(USER_MODULE,
					USER_MODULE_REGISTER, user, null, null);

			if (null != jsonData) {

				try {

					Long uid = jsonData.getLong("uid");

					if (null != uid) {

						user.setUid(uid);
						Global.user = user;

						result = true;

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}

		}

		return result;

	}

	/**
	 * @param user
	 * @return
	 */
	public boolean updateUserInfo(User user) {

		boolean result = false;

		if (null != user && null != Global.user) {

			user.setUid(Global.user.getUid());
			JSONObject jsonData = (JSONObject) send(USER_MODULE,
					USER_MODULE_UPDATE_USER_INFO, user, null, null);

			if (null != jsonData) {

				User updatedUser = getUserInfoByUid(Global.user.getUid());

				if (null != updatedUser) {

					Global.user = updatedUser;

					result = true;
				}

			}

		}

		return result;

	}

	/**
	 * @param path
	 * @param fileName
	 * @param uid
	 * @return
	 */
	public String uploadUserPortrait(String path, String fileName) {

		String fileURL = null;

		fileURL = upload(USER_MODULE, path, fileName, null);

		return fileURL;

	}
}
