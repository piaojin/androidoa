package com.piaojin.domain;

/**
 * Task entity. @author MyEclipse Persistence Tools
 */

public class Task implements java.io.Serializable {

	// Fields

	private Integer tid;
	private Employ employByUid;
	private Employ employByEid;
	private String time;
	private String title;
	private String starttime;
	private String endtime;
	private Integer status;

	// Constructors

	/** default constructor */
	public Task() {
	}

	/** full constructor */
	public Task(Employ employByUid, Employ employByEid, String time,String title,
			String starttime, String endtime, Integer status) {
		this.employByUid = employByUid;
		this.employByEid = employByEid;
		this.time = time;
		this.title = title;
		this.starttime = starttime;
		this.endtime = endtime;
		this.status = status;
	}

	// Property accessors

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTid() {
		return this.tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public Employ getEmployByUid() {
		return this.employByUid;
	}

	public void setEmployByUid(Employ employByUid) {
		this.employByUid = employByUid;
	}

	public Employ getEmployByEid() {
		return this.employByEid;
	}

	public void setEmployByEid(Employ employByEid) {
		this.employByEid = employByEid;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStarttime() {
		return this.starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return this.endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}