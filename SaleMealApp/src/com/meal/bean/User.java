package com.meal.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.meal.util.SysUtil;

/**
 * @author xiamingxing
 * 
 */
public class User implements Serializable {

	private String address;

	private ArrayList<Long> messageList;

	private String name;

	private ArrayList<Long> orderList;

	private String passWord;

	private String phone;

	private String portrait;

	private Long uid;

	private User() {

	}

	public User(String name, String passWord, String portrait, String phone,
			String address) {

		this.uid = -1l;

		this.name = name;

		this.passWord = SysUtil.getMD5(passWord);

		this.portrait = (null != portrait) ? portrait
				: Constant.USER_PORTRAIT_DEFAULT_URL;

		this.phone = phone;

		this.address = address;

		this.orderList = null;

		this.messageList = null;

	}

	public String getAddress() {
		return address;
	}

	public ArrayList<Long> getMessageList() {
		return messageList;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Long> getOrderList() {
		return orderList;
	}

	public String getPassWord() {
		return passWord;
	}

	public String getPhone() {
		return phone;
	}

	public String getPortrait() {
		return portrait;
	}

	public Long getUid() {
		return uid;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setMessageList(ArrayList<Long> messageList) {
		this.messageList = messageList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrderList(ArrayList<Long> orderList) {
		this.orderList = orderList;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

}
