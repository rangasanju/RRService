package com.railways.tayal.rr.model;

import java.util.ArrayList;

public class FingerPrintData {
	
	private String userid;
	private ArrayList<String> fingerprints = new ArrayList<String>();
	
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public ArrayList<String> getFingerprints() {
		return fingerprints;
	}
	public void setFingerprints(ArrayList<String> fingerprints) {
		this.fingerprints = fingerprints;
	}
	
	
	
	

}
