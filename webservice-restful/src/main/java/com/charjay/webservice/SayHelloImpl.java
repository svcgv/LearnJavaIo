
package com.charjay.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SayHelloImpl", targetNamespace = "http://webservice.charjay.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SayHelloImpl {


    /**
     * 
     * @param arg0
     * @return
     *     returns com.charjay.webservice.SayHelloVo
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sayHello2", targetNamespace = "http://webservice.charjay.com/", className = "com.charjay.webservice.SayHello2")
    @ResponseWrapper(localName = "sayHello2Response", targetNamespace = "http://webservice.charjay.com/", className = "com.charjay.webservice.SayHello2Response")
    @Action(input = "http://webservice.charjay.com/SayHelloImpl/sayHello2Request", output = "http://webservice.charjay.com/SayHelloImpl/sayHello2Response")
    public SayHelloVo sayHello2(
            @WebParam(name = "arg0", targetNamespace = "")
                    SayHelloParam arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://webservice.charjay.com/", className = "com.charjay.webservice.SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://webservice.charjay.com/", className = "com.charjay.webservice.SayHelloResponse")
    @Action(input = "http://webservice.charjay.com/SayHelloImpl/sayHelloRequest", output = "http://webservice.charjay.com/SayHelloImpl/sayHelloResponse")
    public String sayHello(
            @WebParam(name = "arg0", targetNamespace = "")
                    String arg0);

}
