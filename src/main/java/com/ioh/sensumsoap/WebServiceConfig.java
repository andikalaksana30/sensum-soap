package com.ioh.sensumsoap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/*");
	}

	@Bean(name = "sensum")
	public DefaultWsdl11Definition sensumWsdl11Definition(XsdSchema schema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("SensumPort");
		wsdl11Definition.setLocationUri("/sensum_ws");
		wsdl11Definition.setTargetNamespace("http://www.ioh.com/api/model/sensum");
		wsdl11Definition.setSchema(schema);
		return wsdl11Definition;
	}
	
	@Bean
	public XsdSchema countriesSchema() {
		return new SimpleXsdSchema(new ClassPathResource("sensum.xsd"));
	}
}