package com.ioh.model;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "status",
    "description"
})
@XmlRootElement(name = "sensum")
public class RestSensumResponse {
    @XmlElement(required = true)
    protected BigInteger status;
    @XmlElement(required = true)
    protected String description;

    /*
    * GETTER METHODS
    */
    public BigInteger getStatus() {
        return status;
    }
    
    public String getDescription() {
        return description;
    }
}