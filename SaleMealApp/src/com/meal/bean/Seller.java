package com.meal.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.meal.util.SysUtil;

/**
 * @author xiamingxing
 * 
 */
public class Seller implements Serializable {

	private String address;

	private String description;

	private String logo;

	private ArrayList<Long> menuList;

	private ArrayList<Long> messageList;

	private double minCost; // 团购最低限额

	private String name;

	private Boolean orderFunctionSwitch;

	private ArrayList<Long> orderList;

	private Long score;
	
	private String passWord;

	private String phone;

	private Long sid;

	private String type;

	private Seller() {
	}

	public Seller(String name, String passWord, String logo,
			String description, String phone, String address, String type,
			double minCost) {

		this.sid = -1l;

		this.name = name;

		this.passWord = SysUtil.getMD5(passWord);

		this.phone = phone;

		this.address = address;

		this.logo = logo != null ? logo : Constant.SELLER_LOGO_DEFAULT_URL;

		this.description = description;

		this.menuList = null;

		this.orderList = null;

		this.orderFunctionSwitch = false;

		this.type = type;

		this.messageList = null;
		
		this.setScore(-1L);

		this.minCost = minCost;
	}

	public String getAddress() {
		return address;
	}

	public String getDescription() {
		return description;
	}

	public String getLogo() {
		return logo;
	}

	public ArrayList<Long> getMenuList() {
		return menuList;
	}

	public ArrayList<Long> getMessageList() {
		return messageList;
	}

	public double getMinCost() {
		return minCost;
	}

	public String getName() {
		return name;
	}

	public Boolean getOrderFunctionSwitch() {
		return orderFunctionSwitch;
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

	public Long getSid() {
		return sid;
	}

	public String getType() {
		return type;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setMenuList(ArrayList<Long> menuList) {
		this.menuList = menuList;
	}

	public void setMessageList(ArrayList<Long> messageList) {
		this.messageList = messageList;
	}

	public void setMinCost(double minCost) {
		this.minCost = minCost;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrderFunctionSwitch(Boolean orderFunctionSwitch) {
		this.orderFunctionSwitch = orderFunctionSwitch;
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

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

}
