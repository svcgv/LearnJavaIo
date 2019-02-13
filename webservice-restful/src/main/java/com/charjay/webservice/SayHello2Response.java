
package com.charjay.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>sayHello2Response complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="sayHello2Response">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://webservice.charjay.com/}sayHelloVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sayHello2Response", propOrder = {
    "_return"
})
public class SayHello2Response {

    @XmlElement(name = "return")
    protected SayHelloVo _return;

    /**
     * 获取return属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SayHelloVo }
     *     
     */
    public SayHelloVo getReturn() {
        return _return;
    }

    /**
     * 设置return属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SayHelloVo }
     *     
     */
    public void setReturn(SayHelloVo value) {
        this._return = value;
    }

}
