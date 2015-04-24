package com.piaojin.domain;

/**
 * Schedule entity. @author MyEclipse Persistence Tools
 */

public class Schedule implements java.io.Serializable {

	// Fields

	private Integer sid;
	private Integer uid;
	private String title;
	private String content;
	private String time;
	private String remindtime;
	private Integer status;
	private Integer isremind;
	private String endtime;

	// Constructors

	/** default constructor */
	public Schedule() {
	}

	// Property accessors

	public Integer getSid() {
		return this.sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRemindtime() {
		return this.remindtime;
	}

	public void setRemindtime(String remindtime) {
		this.remindtime = remindtime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsremind() {
		return this.isremind;
	}

	public void setIsremind(Integer isremind) {
		this.isremind = isremind;
	}

	public String getEndtime() {
		return this.endtime;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

}