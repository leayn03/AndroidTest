package com.meal.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.meal.util.SysUtil;

/**
 * @author xiamingxing
 * 
 */
public class Order implements Serializable {

	private String address;

	private ArrayList<Long> menuList;

	private Long oid;

	private double pay;

	private String phone;

	private short score;

	private String status;

	private String time;

	private Long uid;

	private Order() {

	}

	public Order(String phone, String address, double pay,
			ArrayList<Long> menuList) {

		this.oid = -1l;

		this.status = null;

		this.menuList = menuList;

		this.time = SysUtil.getTime();

		this.score = 0;

		this.pay = pay;

		this.address = address;

		this.phone = phone;

		this.setUid((null != Global.user) ? Global.user.getUid() : -1l);

	}

	public String getAddress() {
		return address;
	}

	public ArrayList<Long> getMenuList() {
		return menuList;
	}

	public Long getOid() {
		return oid;
	}

	public double getPay() {
		return pay;
	}

	public String getPhone() {
		return phone;
	}

	public short getScore() {
		return score;
	}

	public String getStatus() {
		return status;
	}

	public String getTime() {
		return time;
	}

	public Long getUid() {
		return uid;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setMenuList(ArrayList<Long> menuList) {
		this.menuList = menuList;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public void setPay(double pay) {
		this.pay = pay;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setScore(short score) {
		this.score = score;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

}
