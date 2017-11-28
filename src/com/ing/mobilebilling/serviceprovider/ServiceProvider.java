package com.ing.mobilebilling.serviceprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.ing.mobilebilling.daoservices.BillingDAOServices;
import com.ing.mobilebilling.exceptions.BillingServicesDownException;
import com.ing.mobilebilling.services.BillingServices;



public class ServiceProvider {
	private static Properties billingProperties;
	static{
		try{
			billingProperties=new Properties();
			FileInputStream src=new FileInputStream(new File(".\\resource\\CGBillingSystem.properties"));
			billingProperties.load(src);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
		@SuppressWarnings("rawtypes")
		public static BillingServices getBillingServices()throws BillingServicesDownException{
			try{
				String billingservices=billingProperties.getProperty("billingServices");
				Class c= Class.forName(billingservices);
				return(BillingServices)c.newInstance();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
				throw new BillingServicesDownException("Billing services temporarily not available",e);
				
			}catch(InstantiationException e){
				e.printStackTrace();
				throw new BillingServicesDownException("Billing services temporarily not available",e);
			}catch(IllegalAccessException e){
				e.printStackTrace();
				throw new BillingServicesDownException("Billing services temporarily not available",e);
			}
		}
			@SuppressWarnings("rawtypes")
			public static BillingDAOServices getBillingDAOServices()throws BillingServicesDownException{
				try{
					String billingdaoservices=billingProperties.getProperty("billingDAOServices");
					Class c= Class.forName( billingdaoservices);
					return(BillingDAOServices)c.newInstance();
				}catch(ClassNotFoundException e){
					e.printStackTrace();
					throw new BillingServicesDownException("Billing services temporarily not available",e);
					
				}catch(InstantiationException e){
					e.printStackTrace();
					throw new BillingServicesDownException("Billing services temporarily not available",e);
				}catch(IllegalAccessException e){
					e.printStackTrace();
					throw new BillingServicesDownException("Billing services temporarily not available",e);
				}
				
	}

	

}

