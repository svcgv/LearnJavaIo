package com.charjay.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 */
public class HelloClient {

    public static void main(String[] args) {
        try {
            ISayHello hello=(ISayHello) Naming.lookup("rmi://localhost:8888/sayHello");
            System.out.println(hello);
            System.out.println(hello.sayHello("hello, 菲菲"));
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
