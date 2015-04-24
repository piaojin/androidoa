package com.piaojin.domain;

/**
 * Uploadfile entity. @author MyEclipse Persistence Tools
 */

public class Uploadfile implements java.io.Serializable {

	// Fields

	private Integer upid;
	private Integer kid;//表示服务器端数据库中该文件的id

	public Integer getKid() {
		return kid;
	}

	public void setKid(Integer kid) {
		this.kid = kid;
	}

	private Integer uid;
	private String uploadurl;
	private String saveurl;
	private String filename;
	private Double filesize;
	private Double completedsize;
	private Integer iscomplete;
	private String completedate;
	private String startdate;
	private String errormessage;
	private Integer filestatus;
	private String absoluteurl;//文件在手机的本地地址(未上传时的地址)
	private Integer fid;

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getAbsoluteurl() {
		return absoluteurl;
	}

	public void setAbsoluteurl(String absoluteurl) {
		this.absoluteurl = absoluteurl;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
	// Constructors

	/** default constructor */
	public Uploadfile() {
	}


	/** full constructor */
	public Uploadfile(String uploadurl, String saveurl,
					  String filename, Double filesize, Double completedsize,
					  Integer iscomplete, String completedate, String startdate,
					  String errormessage, Integer filestatus) {
		this.uploadurl = uploadurl;
		this.saveurl = saveurl;
		this.filename = filename;
		this.filesize = filesize;
		this.completedsize = completedsize;
		this.iscomplete = iscomplete;
		this.completedate = completedate;
		this.startdate = startdate;
		this.errormessage = errormessage;
		this.filestatus = filestatus;
	}

	// Property accessors

	public Integer getUpid() {
		return this.upid;
	}

	public void setUpid(Integer upid) {
		this.upid = upid;
	}

	public String getUploadurl() {
		return this.uploadurl;
	}

	public void setUploadurl(String uploadurl) {
		this.uploadurl = uploadurl;
	}

	public String getSaveurl() {
		return this.saveurl;
	}

	public void setSaveurl(String saveurl) {
		this.saveurl = saveurl;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Double getFilesize() {
		return this.filesize;
	}

	public void setFilesize(Double filesize) {
		this.filesize = filesize;
	}

	public Double getCompletedsize() {
		return this.completedsize;
	}

	public void setCompletedsize(Double completedsize) {
		this.completedsize = completedsize;
	}

	public Integer getIscomplete() {
		return this.iscomplete;
	}

	public void setIscomplete(Integer iscomplete) {
		this.iscomplete = iscomplete;
	}

	public String getCompletedate() {
		return this.completedate;
	}

	public void setCompletedate(String completedate) {
		this.completedate = completedate;
	}

	public String getStartdate() {
		return this.startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getErrormessage() {
		return this.errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	public Integer getFilestatus() {
		return this.filestatus;
	}

	public void setFilestatus(Integer filestatus) {
		this.filestatus = filestatus;
	}

}