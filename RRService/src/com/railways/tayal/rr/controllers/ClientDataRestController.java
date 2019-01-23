package com.railways.tayal.rr.controllers;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ClientDataRestController {
	
	
	

	@RequestMapping(value="/authenticateClient", method = RequestMethod.GET)
	public ModelAndView initiateBio() {
		
		ModelAndView model = new ModelAndView("Wait");
		return model;
	}

	

	@RequestMapping(value="/uploadImage", method = RequestMethod.GET)
	public ModelAndView uploadImage() {

		ModelAndView model = new ModelAndView("UploadImage");
			
		return model;
	}


	
	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	public ModelAndView saveImage(@RequestParam("file") MultipartFile file) {
		
		ModelAndView model;
		

		if (!file.isEmpty()) {
			try {
				
				
				if(! file.getContentType().equals("image/jpeg"))
				{
					model = new ModelAndView("UploadFail");
					return model;
				}
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = File.separator + "var" + File.separator + "www" + File.separator + "img";
				File dir = new File(rootPath);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(rootPath + File.separator + "runningroom.jpg");
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","echo '111' | sudo -S chmod 755 /var/www/img/runningroom.jpg"});
				
				System.out.println("Server File Location="	+ serverFile.getAbsolutePath());

				model = new ModelAndView("UploadSuccess");
				return model;
			} catch (Exception e) {
				System.out.println("You failed to upload image => " + e.getMessage());
				model = new ModelAndView("UploadFail");
				return model;
			}
		} else {
			
			System.out.println("You failed to upload image because the file was empty.");
			model = new ModelAndView("UploadFail");
			return model;
		}
	}

	
	
	
	
	
	@RequestMapping(value="/getMacAddress", method = RequestMethod.GET)
	public @ResponseBody String processAJAXRequest(HttpServletRequest request, HttpServletResponse response) {
	        
	        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ClientDataRestController - getMacAddress  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	        String macaddress = null;
	        try {
	        	
	        	
	        	final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
	            while (e.hasMoreElements()) {
	                final byte [] mac = e.nextElement().getHardwareAddress();
	                if (mac != null) {
	                    StringBuilder sb = new StringBuilder();
	                    for (int i = 0; i < mac.length; i++)
	                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
	                    
	                    macaddress = sb.toString();
	                    System.out.println(macaddress);
	                    break;
	                }
	            }
	            
	           

	        } catch (Exception e) {

	            e.printStackTrace();

	        }
	        
	        
	        
	        // Process the request
	        // Prepare the response string
	        return macaddress;
	    }

/*
	
	
	
	@RequestMapping(value="/getMacAddress", method = RequestMethod.GET)
	public @ResponseBody void getMacAddress(HttpServletRequest request, HttpServletResponse response) {
	   

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ClientDataRestController - getMacAddress >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		PrintWriter out=null;
		InetAddress ip;
        try {
        	
        	ClientData clientData = new ClientData();
        	ArrayList<String> macs = new ArrayList<String>();
        	
        	
        	out = response.getWriter();
        	
        	
        	final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                final byte [] mac = e.nextElement().getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++)
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    macs.add(sb.toString());
                    System.out.println(sb.toString());
                }
            }
            
            clientData.setMac_address(macs);
        	out.write(macs);

        } catch (Exception e) {

            e.printStackTrace();

        }
out.close();
	
	}
	
	*/
	public static String getSystemMac(){
        try{
                String mac=getMAC4Linux("eth0");
                if(mac==null){
                    mac=getMAC4Linux("eth1");
                    if(mac==null){
                        mac=getMAC4Linux("eth2");
                        if(mac==null){
                            mac=getMAC4Linux("usb0");
                        }
                    }
                }	
                return mac;
            
        }
        catch(Exception E){
            System.err.println("System Mac Exp : "+E.getMessage());
            return null;
        }
    }
    
	
	private static String getMAC4Linux(String name){
        try {
            NetworkInterface network = NetworkInterface.getByName(name);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++){
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
            }
            return (sb.toString());
        }
        catch (Exception E) {
            System.err.println("System Linux MAC Exp : "+E.getMessage());
            return null;
        } 
    } 	

}
