package com.ing.mobilebilling.services;
import java.util.List;

import com.ing.mobilebilling.beans.Bill;
import com.ing.mobilebilling.beans.Customer;
import com.ing.mobilebilling.beans.Plan;
import com.ing.mobilebilling.beans.PostpaidAccount;
import com.ing.mobilebilling.exceptions.BillDetailsNotFoundException;
import com.ing.mobilebilling.exceptions.BillingServicesDownException;
import com.ing.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.ing.mobilebilling.exceptions.InvalidBillMonthException;
import com.ing.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.ing.mobilebilling.exceptions.PostpaidAccountNotFoundException;
public interface BillingServices {
	
	List<Plan> getPlanAllDetails() throws BillingServicesDownException;
	
	int acceptCustomerDetails(String firstName, String lastName, String emailID, String dateOfBirth, String billingAddressCity, String billingAddressState, int billingAddressPinCode, String homeAddressCity, String homeAddressState, int homeAddressPinCode) throws BillingServicesDownException;

	long openPostpaidMobileAccount(int customerID, int planID) 
			throws PlanDetailsNotFoundException,CustomerDetailsNotFoundException,
			BillingServicesDownException;
	
	int  generateMonthlyMobileBill(int customerID, long mobileNo, String billMonth, int noOfLocalSMS, int noOfStdSMS, int noOfLocalCalls, int noOfStdCalls,int internetDataUsageUnits) 
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, 
			InvalidBillMonthException, BillingServicesDownException, 
			PlanDetailsNotFoundException;
	
	Customer getCustomerDetails(int customerID)
			throws CustomerDetailsNotFoundException, BillingServicesDownException;
	
	List<Customer> getAllCustomerDetails() throws BillingServicesDownException;
	
	PostpaidAccount getPostPaidAccountDetails(int customerID, long mobileNo) 
			throws CustomerDetailsNotFoundException, 
			PostpaidAccountNotFoundException, 
			BillingServicesDownException;
	
	List<PostpaidAccount> getCustomerAllPostpaidAccountsDetails(int customerID)
			throws CustomerDetailsNotFoundException, BillingServicesDownException;
	
	Bill getMobileBillDetails(int customerID, long mobileNo, String billMonth)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, 
			InvalidBillMonthException, BillDetailsNotFoundException, BillingServicesDownException;
	
	List<Bill> getCustomerPostPaidAccountAllBillDetails(int customerID, long mobileNo) 
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, 
			BillingServicesDownException, BillDetailsNotFoundException;
	
	boolean changePlan(int customerID, long mobileNo, int planID)
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, 
			PlanDetailsNotFoundException, BillingServicesDownException;
	
	boolean closeCustomerPostPaidAccount(int customerID, long mobileNo) 
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, 
			BillingServicesDownException;
	
	boolean deleteCustomer(int customerID) 
			throws BillingServicesDownException, CustomerDetailsNotFoundException;
	
	Plan getCustomerPostPaidAccountPlanDetails(int customerID, long mobileNo) 
			throws CustomerDetailsNotFoundException, PostpaidAccountNotFoundException, 
			BillingServicesDownException, PlanDetailsNotFoundException ;
}