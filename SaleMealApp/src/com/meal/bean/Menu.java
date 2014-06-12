package com.meal.bean;

import java.io.Serializable;

/**
 * @author xiamingxing
 * 
 */
public class Menu implements Serializable{

	private double discount;

	private Long mid;

	private String name;

	private String photo;

	private double price;

	private Long sid;

	private Menu() {

	}

	public Menu(String name, String photo, double price, double discount) {

		this.mid = -1l;

		this.name = name;

		this.photo = (null != photo) ? photo : Constant.MENU_PHOTO_DEFAULT_URL;

		this.price = price;

		this.discount = discount;

		this.sid = (null != Global.seller) ? Global.seller.getSid() : -1l;

	}

	public double getDiscount() {
		return discount;
	}

	public Long getMid() {
		return mid;
	}

	public String getName() {
		return name;
	}

	public String getPhoto() {
		return photo;
	}

	public double getPrice() {
		return price;
	}

	public Long getSid() {
		return sid;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

}
