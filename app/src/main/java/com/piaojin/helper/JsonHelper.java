package com.piaojin.helper;

public class JsonHelper implements java.io.Serializable{

	private int type;
	private int fid;
	private int kid;
	private double completedsize;
	private int iscomplete;
	private String errormessage;
	private String completedate;
	private int filestatus;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public int getKid() {
		return kid;
	}
	public void setKid(int kid) {
		this.kid = kid;
	}
	public double getCompletedsize() {
		return completedsize;
	}
	public void setCompletedsize(double completedsize) {
		this.completedsize = completedsize;
	}
	public int getIscomplete() {
		return iscomplete;
	}
	public void setIscomplete(int iscomplete) {
		this.iscomplete = iscomplete;
	}
	public String getErrormessage() {
		return errormessage;
	}
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}
	public String getCompletedate() {
		return completedate;
	}
	public void setCompletedate(String completedate) {
		this.completedate = completedate;
	}
	public int getFilestatus() {
		return filestatus;
	}
	public void setFilestatus(int filestatus) {
		this.filestatus = filestatus;
	}
}
