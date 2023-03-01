package com.ioh.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orderType",
    "contactInfo",
    "delayMinutes"
})
@XmlRootElement(name = "sensum")
public class RestSensumRequest {
    @XmlElement(required = true)
    protected String orderType;
    @XmlElement(required = true)
    protected List<RestSensumRequest.ContactInfo> contactInfo;
    @XmlElement(required = true)
    protected BigInteger delayMinutes;

    /*
    * GETTER METHODS
    */
    public String getOrderType() {
        return orderType;
    }
    
    public List<RestSensumRequest.ContactInfo> getContactInfo() {
        if (contactInfo == null) {
            contactInfo = new ArrayList<RestSensumRequest.ContactInfo>();
        }
        return this.contactInfo;
    }

    public BigInteger getDelayMinutes() {
        return delayMinutes;
    }

    /*
    * SETTER METHODS
    */
    public void setOrderType(String value) {
        this.orderType = value;
    }

    public void setDelayMinutes(BigInteger value) {
        this.delayMinutes = value;
    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "key",
        "value"
    })
    public static class ContactInfo {
        @XmlElement(required = true)
        protected String key;
        @XmlElement(required = true)
        protected String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setKey(String value) {
            this.key = value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}