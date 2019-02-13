package com.charjay.catalina.servlets;

import com.alibaba.fastjson.JSON;
import com.charjay.catalina.http.MyRequest;
import com.charjay.catalina.http.MyResponse;
import com.charjay.catalina.http.MyServlet;

public class SecondServlet extends MyServlet {

	@Override
	public void doGet(MyRequest request, MyResponse response) {
		doPost(request, response);
	}
	
	@Override
	public void doPost(MyRequest request, MyResponse response) {
	    String str = JSON.toJSONString(request.getParameters(),true);  
	    response.write(str,200);
	}
	
}
