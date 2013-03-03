package com.adyen.demo;

import java.io.FileInputStream;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import com.adyen.payment.client.Amount;
import com.adyen.payment.client.Card;
import com.adyen.payment.client.ObjectFactory;
import com.adyen.payment.client.Payment;
import com.adyen.payment.client.PaymentPortType;
import com.adyen.payment.client.PaymentRequest;
import com.adyen.payment.client.PaymentResult;

public class DemoAuth {

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
			
			Amount amount=new Amount();
			amount.setValue(Long.parseLong(props.getProperty("amount.value")));
			amount.setCurrency(props.getProperty("amount.currency"));

			Card card = new Card();
			card.setCvc(props.getProperty("card.cvc"));
			card.setExpiryMonth(props.getProperty("card.expiryMonth"));
			card.setExpiryYear(props.getProperty("card.expiryYear"));
			card.setHolderName(props.getProperty("card.holderName"));
			card.setNumber(props.getProperty("card.number"));

			PaymentRequest pr = new PaymentRequest();
			pr.setAmount(of.createPaymentRequestAmount(amount));
			pr.setCard(of.createPaymentRequestCard(card));
			pr.setMerchantAccount(of.createPaymentRequestMerchantAccount(props.getProperty("merchantAccount")));
			pr.setReference(of.createPaymentRequestReference(props.getProperty("reference")+"-"+System.currentTimeMillis()));
			pr.setShopperIP(of.createPaymentRequestShopperIP(props.getProperty("shopperIP")));
			pr.setShopperReference(of.createPaymentRequestShopperReference(props.getProperty("shopperReference")));

			// Create a dynamic Service instance
			Payment payment = new Payment();
			
			PaymentPortType port = payment.getPort(PaymentPortType.class);
			
			((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, props.getProperty("url"));
			((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, props.getProperty("username"));
			((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, props.getProperty("password"));

			
			PaymentResult result = port.authorise(pr);
			
			System.out.println("Payment result resultCode=" + result.getResultCode().getValue() + " pspReference="+result.getPspReference().getValue()
						+" auth=" + result.getAuthCode().getValue()+" refusalReason="+result.getRefusalReason().getValue());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
