package com.adyen.demo;

import java.io.FileInputStream;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import com.adyen.recurring.client.ObjectFactory;
import com.adyen.recurring.client.Recurring;
import com.adyen.recurring.client.RecurringDetail;
import com.adyen.recurring.client.RecurringDetailsRequest;
import com.adyen.recurring.client.RecurringDetailsResult;
import com.adyen.recurring.client.RecurringPortType;

public class DemoRecurring {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Properties props = new Properties();
			if(args.length==0) {
				props.load(new FileInputStream("demo.properties"));
			} else {
				props.load(new FileInputStream(args[0]));
			}
			ObjectFactory of = new ObjectFactory();
			
			RecurringDetailsRequest rdr = new RecurringDetailsRequest();
			rdr.setMerchantAccount(of.createRecurringDetailsRequestMerchantAccount(props.getProperty("merchantAccount")));
			rdr.setShopperReference(of.createRecurringDetailsRequestShopperReference(props.getProperty("shopperReference")));
			
			Recurring r = new Recurring();
			RecurringPortType port = r.getPort(RecurringPortType.class);
			((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, props.getProperty("recurringUrl"));
			((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, props.getProperty("username"));
			((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, props.getProperty("password"));
			RecurringDetailsResult result = port.listRecurringDetails(rdr);
			
			StringBuilder sb = new StringBuilder();
			if(result.getDetails() != null && result.getDetails().getValue() != null && result.getDetails().getValue().getRecurringDetail() != null) {
				sb.append("ShopperReference=").append(result.getShopperReference().getValue()).append(" details:");				
				for(RecurringDetail rd : result.getDetails().getValue().getRecurringDetail()) {
					sb.append(" reference=");
					sb.append(rd.getRecurringDetailReference().getValue());
				}
			}
			System.out.println("List Recurring result "+sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
