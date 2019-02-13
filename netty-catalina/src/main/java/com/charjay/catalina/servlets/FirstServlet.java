package com.charjay.catalina.servlets;

import com.charjay.catalina.http.MyRequest;
import com.charjay.catalina.http.MyResponse;
import com.charjay.catalina.http.MyServlet;

public class FirstServlet extends MyServlet {

	
	@Override
	public void doGet(MyRequest request, MyResponse response) {
		doPost(request, response);
	}

	
	@Override
	public void doPost(MyRequest request, MyResponse response) {
		String param = "name";  
	    String str = request.getParameter(param);  
	    response.write(param + ":" + str,200);
	}
	
}
