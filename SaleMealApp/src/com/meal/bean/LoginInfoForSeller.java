package com.meal.bean;

import com.meal.util.SysUtil;

public class LoginInfoForSeller {

	private String sellerName;
	private String passWord;

	public LoginInfoForSeller(String sellerName, String passWord) {

		this.sellerName = sellerName;
		this.passWord = SysUtil.getMD5(passWord);

	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

}
