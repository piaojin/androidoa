package com.piaojin.domain;

/**
 * Task entity. @author MyEclipse Persistence Tools
 */

public class Task implements java.io.Serializable {

	// Fields

	private Integer tid;

	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	private Integer kid;
	private Integer eid;//任务接受员工

	public Integer getEid() {
		return eid;
	}

	public void setEid(Integer eid) {
		this.eid = eid;
	}

	private Integer uid;//发布人

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	private Employ employByUid;
	private Employ employByEid;
	private String time;
	private String title;
	private String starttime;
	private String endtime;
	private Integer status;

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