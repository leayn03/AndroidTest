package com.meal.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.meal.util.SysUtil;

/**
 * @author xiamingxing
 * 
 */
public class Groupon implements Serializable {

	private Long gid;// 团购ID

	private String limiteTime;// 团购等待时限

	private double minCost; // 团购最低限额

	private ArrayList<Long> orderList;// 订单列表

	private Long sid;// 团购商家ID

	private String status;// 团购订单状态

	private String time;// 团购发起时间
	
	private Long residueCost;
	
	private Long residueTime;

	private Long uid;// 发起团购者ID

	private Groupon() {

	}

	public Groupon(Long sid, String limiteTime, double minCost, Long oid) {

		this.gid = -1l;

		this.sid = sid;

		this.uid = null != Global.user ? Global.user.getUid() : -1l;

		this.time = SysUtil.getTime();

		this.limiteTime = limiteTime;

		this.minCost = minCost;
		
		this.residueCost = -1l;
		
		this.residueTime = -1l;

		this.orderList = new ArrayList<Long>();

		if (null != oid) {

			orderList.add(oid);

		}

	}

	public Long getGid() {
		return gid;
	}

	public String getLimiteTime() {
		return limiteTime;
	}

	public double getMinCost() {
		return minCost;
	}

	public ArrayList<Long> getOrderList() {
		return orderList;
	}

	public Long getSid() {
		return sid;
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

	public void joinGroupon(Long order) {
		this.orderList.add(order);
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public void setLimiteTime(String limiteTime) {
		this.limiteTime = limiteTime;
	}

	public void setMinCost(double minCost) {
		this.minCost = minCost;
	}

	public void setOrderList(ArrayList<Long> orderList) {
		this.orderList = orderList;
	}

	public void setSid(Long sid) {
		this.sid = sid;
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

	public Long getResidueCost() {
		return residueCost;
	}

	public void setResidueCost(Long residueCost) {
		this.residueCost = residueCost;
	}

	public Long getResidueTime() {
		return residueTime;
	}

	public void setResidueTime(Long residueTime) {
		this.residueTime = residueTime;
	}

}
