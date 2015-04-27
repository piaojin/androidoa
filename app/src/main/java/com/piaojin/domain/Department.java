package com.piaojin.domain;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;


/**
 * Department entity. @author MyEclipse Persistence Tools
 */
public class Department implements java.io.Serializable {

	@Expose
	private Integer did;

	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	private Integer kid;//在服务器端的id
	@Expose
    private Integer uid;
	@Expose
    private String dname;
	@Expose
    private String descript;
    public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
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
