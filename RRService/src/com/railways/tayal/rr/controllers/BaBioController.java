package com.railways.tayal.rr.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.railways.tayal.rr.services.BaBioService;


@Controller
public class BaBioController {
	
	
	@RequestMapping(value="/initiateBio/{userid}/{operation}", method = RequestMethod.GET)
	public ModelAndView submitAdmissionForm(@PathVariable("userid") String userid, @PathVariable("operation") String operation) {

		
		System.out.println("userid : " + userid);
		System.out.println("operation : " + operation);
		
		ModelAndView model = new ModelAndView(operation);	
		model.addObject("userid",userid);
		return model;
	}
	



	@RequestMapping(value="/register/{userid}", method = RequestMethod.GET)
	public @ResponseBody String register(@PathVariable("userid") String userid) {
	        
		 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> BaBioController - register   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		 String res = "Registration Failed";

		try{
			System.out.println(" Userid : " + userid);
		    res = BaBioService.registerFinger(userid);	        
		        
		}catch(Exception e)
		{
			System.out.println("Ex : " + e);
		}

		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  BaBioController - startBreath   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.out.println("\n\n\n\n\n");

		return res;
	        
	    }
	

	@RequestMapping(value="/verify/{userid}", method = RequestMethod.GET)
	public @ResponseBody String verify(@PathVariable("userid") String userid) {
	        
		 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  BaBioController - verify   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		 String res = "Verification Failed";

		try{
			System.out.println(" Userid : " + userid);
		    res = BaBioService.verifyFinger(userid);	        
		        
		}catch(Exception e)
		{
			System.out.println("Ex : " + e);
		}

		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  BaBioController - verify   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.out.println("\n\n\n\n\n");

		return res;
	        
	    }
	
	
	
	
	
	@RequestMapping(value="/initiateBreath", method = RequestMethod.GET)
	public ModelAndView initiateBio() {

		ModelAndView model = new ModelAndView("BiometricVerification");
		
		
	
		return model;
	}

	
	@RequestMapping(value="/breathanalysis", method = RequestMethod.GET)
	public ModelAndView initiateBA() {

		ModelAndView model = new ModelAndView("BreathAnalyser");
		
		
	
		return model;
	}


	@RequestMapping(value="/performBA", method = RequestMethod.GET)
	public @ResponseBody void processAJAXRequest(HttpServletRequest request, HttpServletResponse response) {
	        
		 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  startBreath   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");


		 
		 

		//FIND AND KILL PREVIOUS UNCOMPLETED ba PROCESS IF ANY -- THIS IS JUST TO BE SURE, 
			// BECAUSE PARALLEL ba PROCESSES ARE CAUSING PROBLEMS
		try{
			
			PrintWriter out = response.getWriter();
			
			BaBioService.killProcess("ba");
			ProcessBuilder pb = null;       
			
			pb = new ProcessBuilder("./ba","1","10");	 			 
			pb.directory(new File("/usr/local"));		
			
		    java.lang.Process p = pb.start();

		    String line="";			
		    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));	
		   
		    while (!(line.contains("Exhale time") ) && (line = r.readLine()) != null) 
		    {
		        System.out.println("Line : " + line);
		        out.println(line);
		        out.flush();            	
		    }
		    
			out.flush();
			     
		    r.close();
		    out.close();
		   
		        
		        
		        
		}catch(Exception e)
		{
			System.out.println("Ex : " + e);
		}

		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  startBreath   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.out.println("\n\n\n\n\n");


	        
	    }
	
	
	
	

}
