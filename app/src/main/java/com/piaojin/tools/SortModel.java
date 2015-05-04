package com.piaojin.tools;

public class SortModel {

	private String name;   //显示的数据
	private int kid;//同事的服务器短的id

	public int getKid() {
		return kid;
	}

	public void setKid(int kid) {
		this.kid = kid;
	}

	private String sortLetters;  //显示数据拼音的首字母

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
