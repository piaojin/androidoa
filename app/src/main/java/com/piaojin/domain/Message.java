package com.piaojin.domain;

/**
 * Message entity. @author MyEclipse Persistence Tools
 */

public class Message implements java.io.Serializable {

	// Fields

	private Integer mid;
	private Employ employByReceiverid;
	private Employ employBySenderid;
	private String sendtime;
	private String receivetime;
	private Integer type;
	private String msg;
	private String photourl;
	private String videourl;
	private Integer status;

	// Constructors

	/** default constructor */
	public Message() {
	}

	/** minimal constructor */
	public Message(Employ employByReceiverid, Employ employBySenderid,
			String sendtime, String receivetime, Integer type, Integer status) {
		this.employByReceiverid = employByReceiverid;
		this.employBySenderid = employBySenderid;
		this.sendtime = sendtime;
		this.receivetime = receivetime;
		this.type = type;
		this.status = status;
	}

	/** full constructor */
	public Message(Employ employByReceiverid, Employ employBySenderid,
			String sendtime, String receivetime, Integer type, String msg,
			String photourl, String videourl, Integer status) {
		this.employByReceiverid = employByReceiverid;
		this.employBySenderid = employBySenderid;
		this.sendtime = sendtime;
		this.receivetime = receivetime;
		this.type = type;
		this.msg = msg;
		this.photourl = photourl;
		this.videourl = videourl;
		this.status = status;
	}

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