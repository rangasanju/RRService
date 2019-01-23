package com.railways.tayal.rr.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class DBConnection {
	
	private Connection conn=null;
	private ResultSet res = null;
	
	public Connection getConnection()
	{
		
		
		  String url = "jdbc:mysql://localhost:3306/";
		  String dbName = "rrclient";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "root"; 
		  String password = "";
		  
		  
		  try {
			  
			  
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName,userName,password);
		  
		  } catch (Exception e) {
			  e.printStackTrace();
			  }
		
		  return conn;
		
	}
	

	public ResultSet executeQuery(String query) 
	{
		 Connection conn = null;
		 
		 
		  
		  try {
			  System.out.println("QUERY : " + query);
			  conn = getConnection();
			  Statement st = conn.createStatement();
			  st.setQueryTimeout(5);
			  res = st.executeQuery(query);
		  
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  
		  return res;
		
	}


	public int executeUpdate(String query) 
	{
		 Connection conn = null;
		 
		 int val = 0;
		  
		  try {
			  
			  conn = getConnection();
			  Statement st = conn.createStatement();
			  
			  val = st.executeUpdate(query);
			  
			  if(val==1)
				  System.out.print("Successfully inserted value");
		  
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		 
		
		
		  return val;
		
	}
	
	
	public int executeMyBatch(ArrayList list)
	{
		
		int count = 0;
		
		
		try {
		conn = getConnection();
		Statement st = conn.createStatement();
		String query="";
		Iterator<String> itr = list.iterator();
		while(itr.hasNext())
		{
			query = itr.next();
			System.out.println("QUERY : " + query);
			st.addBatch(query);
		}
		
		int[] stmt_executed = st.executeBatch();
		
		
		
		for(int i=0;i<stmt_executed.length;i++)
		{
			if(stmt_executed[i] >0)
				count++;
		}
		
		
		} catch (Exception e) {
			  e.printStackTrace();
		  }
		  
		
		
		
		return count;
	}
	

	public void closeCon()
	{
		 
		  
		  try {
			  
			 conn.close();

		  
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		 
		
	}
	

}