package com.ioh.sensumsoap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;
import java.util.List;

import com.ioh.api.model.sensum.InvokesensumRequest;
import com.ioh.api.model.sensum.InvokesensumResponse;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

import com.ioh.model.RestSensumRequest;
import com.ioh.model.RestSensumResponse;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.URI;
import java.time.Duration;

@Endpoint
public class SensumEndpoint {
	private static final String NAMESPACE_URI = "http://www.ioh.com/api/model/sensum";
    private static final BigInteger BACKEND_TIMEOUT_STATUS_CODE = BigInteger.valueOf(403);
    private static final String BACKEND_TIMEOUT_STATUS_DESC = "CONNECTION TIMEOUT TO BACKEND";
    private static final BigInteger INTERNAL_ERR_STATUS_CODE = BigInteger.valueOf(100);
    private static final String INTERNAL_ERR_STATUS_DESC = "INTERNAL SERVER ERROR";
    
    @Value("${backend.url}")
    private String POST_URL;
    @Value("${backend.timeout}")
    private Integer BACKEND_TIMEOUT;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invokesensumRequest")
	@ResponsePayload
	public InvokesensumResponse getResponse(@RequestPayload InvokesensumRequest request) {

        // Initialize response variables
        BigInteger respStatus;
        String respDescription;

        // Ambil detail request
        String orderType = request.getOrderType();
        BigInteger delayMinutes = request.getDelayMinutes();
        List<InvokesensumRequest.ContactInfo> contactInfo = request.getContactInfo();

        
        // Lakukan processing untuk construct request berupa plain XML, dan kirim ke REST API
        try {

            // Constructing REST request for backend API
            RestSensumRequest restRq = new RestSensumRequest();
            restRq.setOrderType(orderType); 
            restRq.setDelayMinutes(delayMinutes);
            for (int idx = 0; idx < contactInfo.size(); idx++) {
                RestSensumRequest.ContactInfo elemContact = new RestSensumRequest.ContactInfo();
                elemContact.setKey(contactInfo.get(idx).getKey());
                elemContact.setValue(contactInfo.get(idx).getValue());
                restRq.getContactInfo().add(elemContact);
            }
            
            // Parse REST request object to XML String
            JAXBContext jaxbContext = JAXBContext.newInstance(RestSensumRequest.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(restRq, sw);
            String xmlContent = sw.toString();
            
            // Construct HTTP POST Request object
            HttpRequest httpRq = HttpRequest.newBuilder()
                .uri(new URI(POST_URL))
                .headers("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString(xmlContent))
                .timeout(Duration.ofSeconds(BACKEND_TIMEOUT))
                .build();
                
            // Send the HTTP POST Request
            HttpResponse<String> httpRs = HttpClient.newBuilder().build().send(httpRq, HttpResponse.BodyHandlers.ofString());    
            
            // Parse the HTTP POST Response to Java object
            StringReader httpRsReader = new StringReader(httpRs.body());
            JAXBContext jaxbHttpRsCtx = JAXBContext.newInstance(RestSensumResponse.class);
            Unmarshaller jaxbUnmarshaller = jaxbHttpRsCtx.createUnmarshaller();
            RestSensumResponse restResp = (RestSensumResponse) jaxbUnmarshaller.unmarshal(httpRsReader);

            // Map the backend response
            respStatus = restResp.getStatus();
            respDescription = restResp.getDescription();

        } 
        catch (HttpConnectTimeoutException e) { 
            respStatus = BACKEND_TIMEOUT_STATUS_CODE;
            respDescription = BACKEND_TIMEOUT_STATUS_DESC;
        }
        catch (Exception e) {
            e.printStackTrace();
            
            respStatus = INTERNAL_ERR_STATUS_CODE;
            respDescription = INTERNAL_ERR_STATUS_DESC;
        }

        InvokesensumResponse response = new InvokesensumResponse();
        response.setStatus(respStatus);
        response.setDescription(respDescription);
        return response;
		
	}

}