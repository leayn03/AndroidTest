package com.meal.bean;

import java.io.Serializable;

import com.meal.util.SysUtil;

/**
 * @author xiamingxing
 * 
 */
public class Message implements Serializable{

	private String content;

	private Long meid;

	private String receiver;

	private String sender;

	private String status;

	private String time;

	private Message() {

	}

	public Message(String content, String receiver) {

		this.meid = -1l;

		this.content = content;

		this.time = SysUtil.getTime();

		this.status = null;

		if (null != Global.user) {

			this.sender = "user_" + Global.user.getUid();

		} else if (null != Global.seller) {

			this.sender = "seller_" + Global.seller.getSid();

		} else {

			this.sender = "unKnown";

		}

		this.receiver = receiver;

	}

	public String getContent() {
		return content;
	}

	public Long getMeid() {
		return meid;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public String getSender() {
		return sender;
	}

	public String getStatus() {
		return status;
	}

	public String getTime() {
		return time;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setMeid(Long meid) {
		this.meid = meid;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
