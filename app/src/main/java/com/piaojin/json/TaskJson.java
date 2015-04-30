package com.piaojin.json;

/**
 * Created by piaojin on 2015/4/30.
 */
public class TaskJson implements java.io.Serializable{
    int kid;//任务对应在服务器端的数据中的id
    int status;//任务状态

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
