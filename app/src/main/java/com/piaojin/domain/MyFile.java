package com.piaojin.domain;

/**
 * Created by piaojin on 2015/4/20.
 */
public class MyFile implements java.io.Serializable{
    // Fields

    private Integer fid;
    private Integer uid;
    private Integer type;
    private String url;
    private String httpurl;
    private Integer status;
    private String describes;

    private String absoluteurl;
    private String completedate;
    private String name;
    private Double filesize;
    private Double completedsize;
    private Integer iscomplete;

    // Constructors

    public String getAbsoluteurl() {
        return absoluteurl;
    }

    public void setAbsoluteurl(String absoluteurl) {
        this.absoluteurl = absoluteurl;
    }

    public String getCompletedate() {
        return completedate;
    }

    public void setCompletedate(String completedate) {
        this.completedate = completedate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFilesize() {
        return filesize;
    }

    public void setFilesize(Double filesize) {
        this.filesize = filesize;
    }

    public Double getCompletedsize() {
        return completedsize;
    }

    public void setCompletedsize(Double completedsize) {
        this.completedsize = completedsize;
    }

    public Integer getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(Integer iscomplete) {
        this.iscomplete = iscomplete;
    }

    // Property accessors

    public Integer getFid() {
        return this.fid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;

    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpurl() {
        return this.httpurl;
    }

    public void setHttpurl(String httpurl) {
        this.httpurl = httpurl;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescribes() {
        return this.describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }
}
