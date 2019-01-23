package com.railways.tayal.rr.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.jni.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGFingerPosition;
import SecuGen.FDxSDKPro.jni.SGImpressionType;
import SecuGen.FDxSDKPro.jni.SGPPPortAddr;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.railways.tayal.rr.model.FingerPrintData;
import com.railways.tayal.rr.utilities.DBConnection;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class BaBioService {
	
	

public static void killProcess(String process)
{

 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  killProcess   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

 
 ProcessBuilder pb = null;
 Process pr = null;	
	try {
	    Vector<String> commands = new Vector<String>();
	    commands.add("pidof");
	    commands.add(process);
	    pb = new ProcessBuilder(commands);
	    
	    pr = pb.start();
	    pr.waitFor();
	    if (pr.exitValue() == 0){ 
	    BufferedReader outReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	    for (String pid : outReader.readLine().trim().split(" ")) {
	        //log.info("Killing pid: "+pid);
	        Runtime.getRuntime().exec("kill " + pid).waitFor();
	    }
	    outReader.close();
	    
	    }
	   
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}finally
	{		
		pr.destroy();
		
	} 	



System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  killProcess   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
System.out.println("\n\n\n\n\n");



}




public static String getFPData(String webservice_ip, String crewid)
{
	String output="";
	
	 try{
			 System.out.println("Connecting to WS " );
			 
			 String myurl = "http://" + webservice_ip + "/cmsfpws/getfpdata/'" + crewid +"'" ;                                
			 Client client = Client.create();
			 WebResource webresource = client.resource(myurl);					 
			 ClientResponse response1 = webresource.accept("application/json").get(ClientResponse.class);
			 
			 if(response1.getStatus() != 200)
			 {
				 return "failed";
				 
			 }
			 	 
			 output = response1.getEntity(String.class);		 
			 System.out.println("Server Output : " + output);
			 
		 } 
		 catch(Exception e) 
		 {
		     
			 e.printStackTrace();
			 System.out.println("could not connect to remote");
			 
		        
		 } 	 
	
	 return output;
}


public static void parseJSONOutput(String output, String crewid)
{
	 DBConnection db = new DBConnection();
	 Connection conn;

	String[] finger_no = new String[2];
	String[] finger_print = new String[2];

	 
	 // PARSE THE JASON OUTPUT
	 
	 try {
	 //System.out.println("Output from Server .... \n");
	 //System.out.println(output);
	 output =  "{\"fpdata\":" + output + "}";
	 
	 //System.out.println(output);
	 JSONObject jsonobject = new JSONObject(output);		
	 JSONArray tsmarray = (JSONArray) jsonobject.get("fpdata");
	 
	 
	 
	 // EXTRACT THE VALUES
	    finger_no[0] = tsmarray.getJSONObject(0).getString("left_fingre_no");
	    finger_no[1] = tsmarray.getJSONObject(0).getString("right_fingre_no");
	    
	    finger_print[0] = tsmarray.getJSONObject(0).getString("left_print");
	    finger_print[1]	= tsmarray.getJSONObject(0).getString("right_print");
	    
	    
	    byte[] imgarr = null;
	    ByteArrayInputStream bis = null;
	    
	    
	    conn = db.getConnection();  // LOCAL DB
		PreparedStatement ps = null;
		String query = "INSERT INTO FP_Data(USER_ID_V ,FINGER_V ,FINGERPRINT_V,DEVICE_NAME_V,SYNCHED_V) VALUES (?, ?, ?, ?, ?)";
		 
		 
		 
			 
			 for(int i=0; i<2; i++){
			    	
			    	
			    	imgarr = Base64.decodeBase64(finger_print[i]);
				    bis = new ByteArrayInputStream(imgarr);
			    	conn.setAutoCommit(false);
			        
			        ps = conn.prepareStatement(query);
			        ps.setString(1, crewid);
			        ps.setString(2, finger_no[i]);
			        ps.setBinaryStream(3, bis);
			        ps.setString(4, "Bio");
			        ps.setString(5, "Y");
			        ps.executeUpdate();
			        conn.commit();
			       
			        imgarr = null;
			        bis = null;
			        
			    }
			 
			 ps.close();
		     conn.close();
		
		 } 
		 catch(Exception e)
		 {
			 System.out.println("Error : " + e);
		 }
		 finally 
		 {
		       
		        
		 } 
		 
	
}



public static String initiateBio(String crewid, String rereg)
{

 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  initiateBio   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

 DBConnection db = new DBConnection();
 ResultSet rs=null;
 ResultSet rsr=null;
 String res = "";
				
 try{
	 

	 rs = db.executeQuery("SELECT * FROM FP_Data WHERE crewid_v='" + crewid + "'");	 
	 	 
	
	 if(!rereg.equals("true"))		// IF FP DOESNT EXIST IN LOCAL DB , CHECK IN CENTRAL DB
	 {
		 
		 rs = db.executeQuery("SELECT peer_ip_v FROM peers");
		 
		 if(rs.next())  // IF CENTRAL SERVER IP IS CONFIGURED IN THIS THIN CLIENT
		 {
			 try{
							 
				 String output = getFPData(rs.getString("peer_ip_v"),crewid);
				 
				
				 
				 if(output.equals("[null]"))	// IF FP NOT FOUND ON CENTRAL SERVER - START REGISTRATION
				 {
					 res = "bioreg";
					 System.out.println("WS returned null");
				 }
				 else							// IF FP FOUND ON CENTRAL SERVER
				 {
					 
					 	// PARSE THE JASON OUTPUT
					 
							 parseJSONOutput(output,crewid);
						     
							 //lf.setFirst_finger(finger_no[0]);
							 //lf.setSecond_finger(finger_no[1]);
							 
							 
							 //forward = mapping.findForward("biover");
					 
				 }
					 
							 
				 } 
				 catch(Exception e) 
				 {
				     
					 e.printStackTrace();
					 System.out.println("could not connect to remote");
					 //forward = mapping.findForward("bioreg");
				        
				 } 	 
							 
							 
					 
					 
					 
		 }else	// IF CENTRAL SERVER IP IS NOT CONFIGURED IN THIS THIN CLIENT
		 {
			
			 System.out.println("Centeral Server not configured");
			 //forward = mapping.findForward("bioreg");
		 }
		 
		
	 }else	// IF IT IS A CASE OF RE-REGISTRATION
	 {
			
		 System.out.println("Re - Registration ");
		 //forward = mapping.findForward("bioreg");
	 }
	 
 }catch(Exception e)
 {
	 System.out.println("Error : " + e);
 }
 finally
 {
	 //rs.close();
	 db.closeCon();
	 
 }

 
System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  initiateBio   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
System.out.println("\n\n\n\n\n");

return "BiometricRegistration";

}




public static String registerFinger(String userid)
{
	
	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  registerFinger   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	
    long err;
    byte[] imageBuffer1;
    byte[] ANSIminutiaeBuffer1;
    String str_finer_print ="";
    String res = "FAILED";
   

 
    System.out.println("");
    System.out.println("###############################");
    System.out.println("SecuGen FDx SDK Pro for Java");
    System.out.println("JSGFPLib JNI Library Test Program");
    System.out.println("###############################");
    System.out.println("");
    
    
    ///////////////////////////////////////////////
    // Instantiate JSGFPLib object
    System.out.println("Instantiate JSGFPLib Object");
    JSGFPLib sgfplib = new JSGFPLib();
    if ((sgfplib != null) && (sgfplib.jniLoadStatus != SGFDxErrorCode.SGFDX_ERROR_JNI_DLLLOAD_FAILED))
    {
        System.out.println(sgfplib);
    }
    else
    {
        System.out.println("An error occurred while loading JSGFPLIB.DLL JNI Wrapper");
        res = "An error occurred while loading JSGFPLIB.DLL JNI Wrapper";
        return res;
    }

    ///////////////////////////////////////////////
    // Init()
    System.out.println("Call Init(SGFDxDeviceName.SG_DEV_AUTO)");
    err = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
    System.out.println("Init returned : [" + err + "]");

    ///////////////////////////////////////////////
    // OpenDevice()
    System.out.println("Call OpenDevice(SGPPPortAddr.AUTO_DETECT)");
    err = sgfplib.OpenDevice(SGPPPortAddr.AUTO_DETECT);
    System.out.println("OpenDevice returned : [" + err + "]");

    ///////////////////////////////////////////////
    // GetError()
    System.out.println("Call GetLastError()");
    err = sgfplib.GetLastError();
    System.out.println("GetLastError returned : [" + err + "]");

    ///////////////////////////////////////////////
    // GetDeviceInfo()
    System.out.println("Call GetDeviceInfo()");
    SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
    err = sgfplib.GetDeviceInfo(deviceInfo);
    System.out.println( "GetDeviceInfo returned : [" + err + "]");
    System.out.println("\tdeviceInfo.DeviceSN:    [" + new String(deviceInfo.deviceSN()) + "]");
    System.out.println("\tdeviceInfo.Brightness:  [" + deviceInfo.brightness + "]");
    System.out.println("\tdeviceInfo.ComPort:     [" + deviceInfo.comPort + "]");
    System.out.println("\tdeviceInfo.ComSpeed:    [" + deviceInfo.comSpeed + "]");
    System.out.println("\tdeviceInfo.Contrast:    [" + deviceInfo.contrast + "]");
    System.out.println("\tdeviceInfo.DeviceID:    [" + deviceInfo.deviceID + "]");
    System.out.println("\tdeviceInfo.FWVersion:   [" + deviceInfo.FWVersion + "]");
    System.out.println("\tdeviceInfo.Gain:        [" + deviceInfo.gain + "]");
    System.out.println("\tdeviceInfo.ImageDPI:    [" + deviceInfo.imageDPI + "]");
    System.out.println("\tdeviceInfo.ImageHeight: [" + deviceInfo.imageHeight + "]");
    System.out.println("\tdeviceInfo.ImageWidth:  [" + deviceInfo.imageWidth + "]");
    
   

   


    int[] quality = new int[1];
    int[] maxSize = new int[1];
    int[] size = new int[1];
    SGFingerInfo fingerInfo = new SGFingerInfo();
    fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
    fingerInfo.ImageQuality = quality[0];
    fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
    fingerInfo.ViewNumber = 1;



//////////////////////////////////////////////////////////////////////////////
//Finger 1
    ///////////////////////////////////////////////
    // getImage() - 1st Capture
    System.out.println("Call SetLedOn(true)");
    err =sgfplib.SetLedOn(true);
    System.out.println("SetLedOn returned : [" + err + "]");
    System.out.print("Capture 1. Please place finger on sensor with LEDs on and press <ENTER> ");
    imageBuffer1 = new byte[deviceInfo.imageHeight*deviceInfo.imageWidth];
    try
    {
        //System.in.read(kbBuffer);
        System.out.println("Call GetImageEx()");
        int image_quality = 50; //0 - 100. 50 or above recommended for registration. 40 or above for verification
        int timeout = 10000; //10 seconds
        err = sgfplib.GetImageEx(imageBuffer1,timeout,0,image_quality);
        
        System.out.println("GetImage returned : [" + err + "]");
        if (err == SGFDxErrorCode.SGFDX_ERROR_NONE)
        {
            err = sgfplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
            System.out.println("GetImageQuality returned : [" + err + "]");
            System.out.println("Image Quality is : [" + quality[0] + "]");
            
        }
        else
        {
            System.out.println("ERROR: Fingerprint image capture faile");
            res = "ERROR: Fingerprint image capture faile";//Cannot continue test if image not captured
            return res;
        }
    }
    catch (Exception e)
    {
        System.out.println("Exception reading keyboard : " + e);
    }

    
    ///////////////////////////////////////////////
    // Set Template format ANSI378
    System.out.println("Call SetTemplateFormat(ANSI378)");
    err = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ANSI378);
    System.out.println("SetTemplateFormat returned : [" + err + "]");

    ///////////////////////////////////////////////
    // Get Max Template Size for ANSI378
    System.out.println("Call GetMaxTemplateSize()");
    err = sgfplib.GetMaxTemplateSize(maxSize);
    System.out.println("GetMaxTemplateSize returned : [" + err + "]");
    System.out.println("Max ANSI378 Template Size is : [" + maxSize[0] + "]");

    ///////////////////////////////////////////////
    // Greate ANSI378 Template for Finger1
    ANSIminutiaeBuffer1 = new byte[maxSize[0]];
    System.out.println("Call CreateTemplate()");
    err = sgfplib.CreateTemplate(fingerInfo, imageBuffer1, ANSIminutiaeBuffer1);
    System.out.println("CreateTemplate returned : [" + err + "]");
    err = sgfplib.GetTemplateSize(ANSIminutiaeBuffer1, size);
    System.out.println("GetTemplateSize returned : [" + err + "]");
    System.out.println("ANSI378 Template Size is : [" + size[0] + "]");
    try
    {
        if (err == SGFDxErrorCode.SGFDX_ERROR_NONE)
        {
        	str_finer_print = Base64.encodeBase64String(ANSIminutiaeBuffer1);
        	
        }
    }
    catch (Exception e)
    {
        System.out.println("Exception writing minutiae file : " + e);
        res = "Exception writing minutiae file : " + e;//Cannot continue test if image not captured
        return res;
    }

    
    
    // SEND THE FIGERPRINT DATA TO CENTRAL SERVER
     String server = getServerName();
     String serial = new String(deviceInfo.deviceSN()).trim().toString();
    
     
     //serial = serial.substring(serial.indexOf('[') + 1, serial.indexOf(']'));
     //System.out.println("PRINT : " + serial);
     //System.out.println("PRINT : " + str_finer_print);
	 String myurl = "http://" + server + ":8080/FingerPrintService/saveFingerPrint/" + userid + "/" + serial + "/" + str_finer_print.replaceAll("/", "tayal")    ;
	 System.out.println("PRINT URL : " + myurl);

	 Client client = Client.create();
	 WebResource webresource = client.resource(myurl);
	 
	 ClientResponse response1 = webresource.accept("application/json").get(ClientResponse.class);
	 
	 if(response1.getStatus() != 200)
	 {
		// throw new RuntimeException("Failed : HTTP error code : "	+ response1.getStatus());
		 System.out.println("HTTP error code : "	+ response1.getStatus());
		 
	 }
	 
	 
	 String output = response1.getEntity(String.class);
	 System.out.println("Output from Server .... \n");
	
	 
	 
	 
	 if(output.equals("true"))
		res="SUCCESS";
	 else
	    res="REMOTE STORE FAILED";
	 
	 
     
 ///////////////////////////////////////////////
 // CloseDevice()
 System.out.println("Call CloseDevice()");
 err = sgfplib.CloseDevice();
 System.out.println("CloseDevice returned : [" + err + "]");


 ///////////////////////////////////////////////
 // Close JSGFPLib native library
 System.out.println("Call Close()");
 sgfplib.Close();
 System.out.println("Close returned : [" + err + "]");


 sgfplib = null;
 imageBuffer1 = null;
 ANSIminutiaeBuffer1 = null;
		
	    
    return res;
}






public static String verifyFinger(String userid)
{
	
	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  verifyFinger   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	
	String res="Verification Failed";
	 String server = getServerName();
	 String myurl = "http://" + server + ":8080/FingerPrintService/getFingerPrint/" + userid ;


	 DefaultClientConfig def = new DefaultClientConfig();
	 def.getClasses().add(JacksonJsonProvider.class);
	 
	 Client client = Client.create(def);
	 client.setReadTimeout(5000);
	 WebResource webresource = client.resource(myurl);
	 
	 FingerPrintData response1 = webresource.accept("application/json").get(FingerPrintData.class);
	
	 
	 
	 //FingerPrintData output = response1.getEntity(FingerPrintData.class);
	 ArrayList<String> fingerprints = response1.getFingerprints();
	 
	 
	
	 Iterator<String> itr1 = fingerprints.iterator();
	 
	 while(itr1.hasNext())
	 {
		 System.out.println(itr1.next() + "\n");
		 
	 }
     
	 
	 
	 System.out.println("Output from Server .... \n");
	 
	 
	 
	 
     long err;
     String finger = new String("Finger");
     byte[] imageBuffer1;
     byte[] imageBuffer2;
     byte[] SG400minutiaeBuffer1;
     byte[] ANSIminutiaeBuffer1;
     byte[] ISOminutiaeBuffer1;
     byte[] SG400minutiaeBuffer2;
     byte[] ANSIminutiaeBuffer2;
     byte[] ISOminutiaeBuffer2;


     System.out.println("");
     System.out.println("###############################");
     System.out.println("SecuGen FDx SDK Pro for Java");
     System.out.println("JSGFPLib JNI Library Test Program");
     System.out.println("###############################");
     System.out.println("");
     
     
     ///////////////////////////////////////////////
     // Instantiate JSGFPLib object
     System.out.println("Instantiate JSGFPLib Object");
     JSGFPLib sgfplib = new JSGFPLib();
     if ((sgfplib != null) && (sgfplib.jniLoadStatus != SGFDxErrorCode.SGFDX_ERROR_JNI_DLLLOAD_FAILED))
     {
         System.out.println(sgfplib);
     }
     else
     {
         System.out.println("An error occurred while loading JSGFPLIB.DLL JNI Wrapper");
         res="An error occurred while loading JSGFPLIB.DLL JNI Wrapper";
        
     }

     ///////////////////////////////////////////////
     // Init()
     System.out.println("Call Init(SGFDxDeviceName.SG_DEV_AUTO)");
     err = sgfplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
     System.out.println("Init returned : [" + err + "]");

     ///////////////////////////////////////////////
     // OpenDevice()
     System.out.println("Call OpenDevice(SGPPPortAddr.AUTO_DETECT)");
     err = sgfplib.OpenDevice(SGPPPortAddr.AUTO_DETECT);
     System.out.println("OpenDevice returned : [" + err + "]");

     ///////////////////////////////////////////////
     // GetError()
     System.out.println("Call GetLastError()");
     err = sgfplib.GetLastError();
     System.out.println("GetLastError returned : [" + err + "]");

     ///////////////////////////////////////////////
     // GetDeviceInfo()
     System.out.println("Call GetDeviceInfo()");
     SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
     err = sgfplib.GetDeviceInfo(deviceInfo);
     System.out.println( "GetDeviceInfo returned : [" + err + "]");
     System.out.println("\tdeviceInfo.DeviceSN:    [" + new String(deviceInfo.deviceSN()) + "]");
     System.out.println("\tdeviceInfo.Brightness:  [" + deviceInfo.brightness + "]");
     System.out.println("\tdeviceInfo.ComPort:     [" + deviceInfo.comPort + "]");
     System.out.println("\tdeviceInfo.ComSpeed:    [" + deviceInfo.comSpeed + "]");
     System.out.println("\tdeviceInfo.Contrast:    [" + deviceInfo.contrast + "]");
     System.out.println("\tdeviceInfo.DeviceID:    [" + deviceInfo.deviceID + "]");
     System.out.println("\tdeviceInfo.FWVersion:   [" + deviceInfo.FWVersion + "]");
     System.out.println("\tdeviceInfo.Gain:        [" + deviceInfo.gain + "]");
     System.out.println("\tdeviceInfo.ImageDPI:    [" + deviceInfo.imageDPI + "]");
     System.out.println("\tdeviceInfo.ImageHeight: [" + deviceInfo.imageHeight + "]");
     System.out.println("\tdeviceInfo.ImageWidth:  [" + deviceInfo.imageWidth + "]");
     
    
    
    

     int[] quality = new int[1];
     int[] maxSize = new int[1];
     int[] size = new int[1];
     SGFingerInfo fingerInfo = new SGFingerInfo();
     fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
     fingerInfo.ImageQuality = quality[0];
     fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
     fingerInfo.ViewNumber = 1;



//////////////////////////////////////////////////////////////////////////////
//Finger 1
     ///////////////////////////////////////////////
     // getImage() - 1st Capture
     System.out.println("Call SetLedOn(true)");
     err =sgfplib.SetLedOn(true);
     System.out.println("SetLedOn returned : [" + err + "]");
     System.out.print("Capture 1. Please place [" + finger + "] on sensor with LEDs on and press <ENTER> ");
     imageBuffer1 = new byte[deviceInfo.imageHeight*deviceInfo.imageWidth];
     try
     {
         
         System.out.println("Call getImageEx()");
         
         int image_quality = 50; //0 - 100. 50 or above recommended for registration. 40 or above for verification
         int timeout = 10000; //10 seconds
         err = sgfplib.GetImageEx(imageBuffer1,timeout,0,image_quality);
         
         System.out.println("GetImage returned : [" + err + "]");
         if (err == SGFDxErrorCode.SGFDX_ERROR_NONE)
         {
             err = sgfplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
             System.out.println("GetImageQuality returned : [" + err + "]");
             System.out.println("Image Quality is : [" + quality[0] + "]");
             
         }
         else if (err == SGFDxErrorCode.SGFDX_ERROR_TIME_OUT)
         {
        	 System.out.println("ERROR: TIMEOUT");
             res="ERROR: TIMEOUT";
         }
         else
         {
             System.out.println("ERROR: Fingerprint image capture failed for sample1.");
             res="ERROR: Fingerprint image capture failed for sample1.";
         }
         
     }
     catch (Exception e)
     {
         System.out.println("Exception reading keyboard : " + e);
         res="Exception reading keyboard : " + e;
     }

    

     ///////////////////////////////////////////////
     // Set Template format ANSI378
     System.out.println("Call SetTemplateFormat(ANSI378)");
     err = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ANSI378);
     System.out.println("SetTemplateFormat returned : [" + err + "]");

     ///////////////////////////////////////////////
     // Get Max Template Size for ANSI378
     System.out.println("Call GetMaxTemplateSize()");
     err = sgfplib.GetMaxTemplateSize(maxSize);
     System.out.println("GetMaxTemplateSize returned : [" + err + "]");
     System.out.println("Max ANSI378 Template Size is : [" + maxSize[0] + "]");

     ///////////////////////////////////////////////
     // Greate ANSI378 Template for Finger1
     ANSIminutiaeBuffer1 = new byte[maxSize[0]];
     System.out.println("Call CreateTemplate()");
     err = sgfplib.CreateTemplate(fingerInfo, imageBuffer1, ANSIminutiaeBuffer1);
     System.out.println("CreateTemplate returned : [" + err + "]");
     err = sgfplib.GetTemplateSize(ANSIminutiaeBuffer1, size);
     System.out.println("GetTemplateSize returned : [" + err + "]");
     System.out.println("ANSI378 Template Size is : [" + size[0] + "]");
     
     
     
     boolean[] matched = new boolean[1];
     int[] score = new int[1];
     Iterator<String> itr = fingerprints.iterator();
	 
	 while(itr.hasNext())
	 {
		 
		 //System.out.println(itr.next() + "\n");
		 ANSIminutiaeBuffer2 = Base64.decodeBase64(itr.next());
		 
	     err = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ANSI378);
	     err = sgfplib.MatchAnsiTemplate(ANSIminutiaeBuffer1, 0, ANSIminutiaeBuffer2, 0, SGFDxSecurityLevel.SL_NORMAL, matched);
	     System.out.println("ANSI-1 <> ANSI-2 Match Result : [" + matched[0] + "]");
	     if(matched[0])
	     {
	    	 res = "MATCH";
	    	 break;
	     }
	    	
	     err = sgfplib.GetAnsiMatchingScore(ANSIminutiaeBuffer1, 0, ANSIminutiaeBuffer2, 0, score);
	     System.out.println("ANSI-1  <> ANSI-2 Match Score : [" + score[0] + "]");	     
	     
	 }
     
    
    
     ///////////////////////////////////////////////
     // CloseDevice()
     System.out.println("Call CloseDevice()");
     err = sgfplib.CloseDevice();
     System.out.println("CloseDevice returned : [" + err + "]");


     ///////////////////////////////////////////////
     // Close JSGFPLib native library
     System.out.println("Call Close()");
     sgfplib.Close();
     System.out.println("Close returned : [" + err + "]");

     sgfplib = null;
     imageBuffer1 = null;
     imageBuffer2 = null;
     SG400minutiaeBuffer1 = null;
     ANSIminutiaeBuffer1 = null;
     ISOminutiaeBuffer1 = null;
     SG400minutiaeBuffer2 = null;
     ANSIminutiaeBuffer2 = null;
     ISOminutiaeBuffer2 = null;
	 
	 
	 
	
	 
     System.out.println("RES " + res);
	    
    return res;
}






public static String getServerName()
{
	

	 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  getServerName   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

	 DBConnection db = new DBConnection();
	 ResultSet rs=null;
	 String res = "";
	
	 try{		 
		 rs = db.executeQuery("SELECT peer_ip_v FROM peers");		 
		 
		 if(rs.next())  // IF CENTRAL SERVER IP IS CONFIGURED IN THIS THIN CLIENT
		 {			 
			 res = rs.getString("peer_ip_v");			 					 
		 }else	// IF CENTRAL SERVER IP IS NOT CONFIGURED IN THIS THIN CLIENT
		 {			
			 System.out.println("Centeral Server not configured");
			 res = "Server not configured";
		 }	 
		 
	 } 
	catch(Exception e) 
	{
	    
		 e.printStackTrace();
		 System.out.println("Could not connect to remote");
		 res = "Could not connect to remote";
	       
	} 	 
			
	return res;							 
						 
}


}
