package com.piaojin.domain;

/**
 * Message entity. @author MyEclipse Persistence Tools
 */

public class Message implements java.io.Serializable {

	// Fields

	private Integer mid;

	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	public Integer getReceiverid() {
		return receiverid;
	}

	public void setReceiverid(Integer receiverid) {
		this.receiverid = receiverid;
	}

	public Integer getSenderid() {
		return senderid;
	}

	public void setSenderid(Integer senderid) {
		this.senderid = senderid;
	}

	private Integer kid;
	private Integer receiverid;
	private Integer senderid;
	private Employ employByReceiverid;
	private Employ employBySenderid;
	private String sendtime;
	private String receivetime;
	private Integer type;
	private String msg;
	private String photourl;
	private String videourl;
	private Integer status;
	private String receiverip;

	public String getReceiverip() {
		return receiverip;
	}

	public void setReceiverip(String receiverip) {
		this.receiverip = receiverip;
	}
	// Constructors

	// Property accessors

	public Integer getMid() {
		return this.mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public Employ getEmployByReceiverid() {
		return this.employByReceiverid;
	}

	public void setEmployByReceiverid(Employ employByReceiverid) {
		this.employByReceiverid = employByReceiverid;
	}

	public Employ getEmployBySenderid() {
		return this.employBySenderid;
	}

	public void setEmployBySenderid(Employ employBySenderid) {
		this.employBySenderid = employBySenderid;
	}

	public String getSendtime() {
		return this.sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getReceivetime() {
		return this.receivetime;
	}

	public void setReceivetime(String receivetime) {
		this.receivetime = receivetime;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPhotourl() {
		return this.photourl;
	}

	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

	public String getVideourl() {
		return this.videourl;
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}