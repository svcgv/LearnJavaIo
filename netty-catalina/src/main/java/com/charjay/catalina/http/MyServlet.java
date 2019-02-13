package com.charjay.catalina.http;

public abstract class MyServlet {
	
	public abstract void doGet(MyRequest request, MyResponse response);
	
	public abstract void doPost(MyRequest request, MyResponse response);
}
