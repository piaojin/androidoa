package com.piaojin.domain;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;


/**
 * Department entity. @author MyEclipse Persistence Tools
 */
public class Department implements java.io.Serializable {

	private Integer dpid;

	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	private Integer kid;//在服务器端的id
    private Integer uid;
    private String dname;
    private String descript;
    public Integer getDpid() {
		return dpid;
	}
	public void setDpid(Integer did) {
		this.dpid = dpid;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public Set getEmploies() {
		return emploies;
	}
	public void setEmploies(Set emploies) {
		this.emploies = emploies;
	}
	private Set emploies=new HashSet(0);
   
}
