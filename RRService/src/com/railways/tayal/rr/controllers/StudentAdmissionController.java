package com.railways.tayal.rr.controllers;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StudentAdmissionController {


	@RequestMapping(value="/admissionForm.html", method = RequestMethod.GET)
	public ModelAndView getAdmissionForm() {

		ModelAndView model = new ModelAndView("AdmissionForm");
		
		
		InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            System.out.print("Current MAC address : ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            System.out.println(sb.toString());

        } catch (Exception e) {

            e.printStackTrace();

        }


		return model;
	}

	@RequestMapping(value="/submitAdmissionForm.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(@ModelAttribute("student1") Student student1) {


		ModelAndView model = new ModelAndView("AdmissionSuccess");
		model.addObject("headerMessage","Gontu College of Engineering, India");
		
		return model;
	}
	
	@ResponseBody
	@RequestMapping(value="/students", method = RequestMethod.GET)
	public ArrayList<Student> getStudentList() {

		Student s1 = new Student();
		s1.setStudentName("Khali");
		
		Student s2 = new Student();
		s2.setStudentName("Bali");
		
		Student s3 = new Student();
		s3.setStudentName("Nalli");
		
		ArrayList<Student> list = new ArrayList<Student>();
		
		list.add(s1);
		list.add(s2);
		list.add(s3);
		
		return list;
	}
	
	
}

