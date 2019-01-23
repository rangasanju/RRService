package com.railways.tayal.rr.model;

import java.util.ArrayList;

public class ClientData {
	
	
	private String status;
	ArrayList<String>  mac_address_list = new ArrayList<String>();

	
	
	



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<String> getMac_address_list() {
		return mac_address_list;
	}

	public void setMac_address_list(ArrayList<String> mac_address_list) {
		this.mac_address_list = mac_address_list;
	}


}
