package com.ing.mobilebilling.services;

import java.util.List;

import com.ing.mobilebilling.beans.Address;
import com.ing.mobilebilling.beans.Bill;
import com.ing.mobilebilling.beans.Customer;
import com.ing.mobilebilling.beans.Plan;
import com.ing.mobilebilling.beans.PostpaidAccount;
import com.ing.mobilebilling.daoservices.BillingDAOServices;
import com.ing.mobilebilling.exceptions.BillDetailsNotFoundException;
import com.ing.mobilebilling.exceptions.BillingServicesDownException;
import com.ing.mobilebilling.exceptions.CustomerDetailsNotFoundException;
import com.ing.mobilebilling.exceptions.InvalidBillMonthException;
import com.ing.mobilebilling.exceptions.PlanDetailsNotFoundException;
import com.ing.mobilebilling.exceptions.PostpaidAccountNotFoundException;
import com.ing.mobilebilling.serviceprovider.ServiceProvider;

public class BillingServicesImpl implements BillingServices {
	private BillingDAOServices daoServices;
	
	public BillingServicesImpl(BillingDAOServices daoServices) {
		this.daoServices = daoServices;
	}
	
	//public BillingServicesImpl() throws BillingServicesDownException {
	//	daoServices = ServiceProvider.getBillingDAOServices();
	//}
	@Override
	public List<Plan> getPlanAllDetails() throws BillingServicesDownException {
		return daoServices.getAllPlans();
	}
	@Override
	public int acceptCustomerDetails(String firstName, String lastName,
			String emailID, String dateOfBirth, String billingAddressCity,
			String billingAddressState, int billingAddressPinCode,
			String homeAddressCity, String homeAddressState,
			int homeAddressPinCode) throws BillingServicesDownException {

		return daoServices.insertCustomer(new Customer(firstName, lastName, emailID, dateOfBirth, new Address(billingAddressPinCode, billingAddressCity, billingAddressState), new Address(homeAddressPinCode, homeAddressCity, homeAddressState)));

	}
	@Override
	public long openPostpaidMobileAccount(int customerID, int planID)
			throws PlanDetailsNotFoundException,
			CustomerDetailsNotFoundException, BillingServicesDownException {
		this.getCustomerDetails(customerID);
		Plan plan = daoServices.getPlan(planID);
		if(plan==null) throw new PlanDetailsNotFoundException("Plan does not exist.");
		return daoServices.insertPostPaidAccount(customerID, new PostpaidAccount(daoServices.getPlan(planID)));
	}
	@Override
	public int generateMonthlyMobileBill(int customerID, long mobileNo,
			String billMonth, int noOfLocalSMS, int noOfStdSMS,
			int noOfLocalCalls, int noOfStdCalls, int internetDataUsageUnits)
					throws CustomerDetailsNotFoundException,
					PostpaidAccountNotFoundException, InvalidBillMonthException,
					BillingServicesDownException, PlanDetailsNotFoundException {
		PostpaidAccount postpaidAccount = getPostPaidAccountDetails(customerID, mobileNo);
		Bill bill = new Bill(noOfLocalSMS, noOfStdSMS, noOfLocalCalls, noOfStdCalls, internetDataUsageUnits, billMonth);
		float monthTotalBill=postpaidAccount.getPlan().getMonthlyRental();

		if(postpaidAccount.getPlan().getFreeLocalCalls()<bill.getNoOfLocalCalls()){
			monthTotalBill += ((bill.getNoOfLocalCalls()-postpaidAccount.getPlan().getFreeLocalCalls())*postpaidAccount.getPlan().getLocalCallRate());
			bill.setLocalCallAmount(((bill.getNoOfLocalCalls()-postpaidAccount.getPlan().getFreeLocalCalls())*postpaidAccount.getPlan().getLocalCallRate()));
		}
		if(postpaidAccount.getPlan().getFreeLocalSMS()<bill.getNoOfLocalSMS()){
			monthTotalBill += ((bill.getNoOfLocalSMS()-postpaidAccount.getPlan().getFreeLocalSMS())*postpaidAccount.getPlan().getLocalSMSRate());
			bill.setLocalSMSAmount(((bill.getNoOfLocalSMS()-postpaidAccount.getPlan().getFreeLocalSMS())*postpaidAccount.getPlan().getLocalSMSRate()));
		}	
		if(postpaidAccount.getPlan().getFreeStdCalls()<bill.getNoOfStdCalls()){
			monthTotalBill +=((bill.getNoOfStdCalls()-postpaidAccount.getPlan().getFreeStdCalls())*postpaidAccount.getPlan().getStdCallRate());
			bill.setStdCallAmount(((bill.getNoOfStdCalls()-postpaidAccount.getPlan().getFreeStdCalls())*postpaidAccount.getPlan().getStdCallRate()));
		}
		if(postpaidAccount.getPlan().getFreeStdSMS()<bill.getNoOfStdSMS()){
			monthTotalBill += ((bill.getNoOfStdSMS()-postpaidAccount.getPlan().getFreeStdSMS())*postpaidAccount.getPlan().getStdSMSRate());
			bill.setStdSMSAmount(((bill.getNoOfStdSMS()-postpaidAccount.getPlan().getFreeStdSMS())*postpaidAccount.getPlan().getStdSMSRate()));
		}
		if(postpaidAccount.getPlan().getFreeInternetDataUsageUnits()<bill.getInternetDataUsageUnits()){
			monthTotalBill +=((bill.getInternetDataUsageUnits()-postpaidAccount.getPlan().getFreeInternetDataUsageUnits())*postpaidAccount.getPlan().getInternetDataUsageRate());
			bill.setInternetDataUsageAmount(((bill.getInternetDataUsageUnits()-postpaidAccount.getPlan().getFreeInternetDataUsageUnits())*postpaidAccount.getPlan().getInternetDataUsageRate()));
		}
		bill.setServicesTax(0.133f);
		bill.setVat(0.120f);
		monthTotalBill +=(monthTotalBill*bill.getServicesTax()+monthTotalBill*bill.getVat());
		bill.setTotalBillAmount(monthTotalBill);
		return daoServices.insertMonthlybill(customerID, mobileNo, bill);
	}
	@Override
	public Customer getCustomerDetails(int customerID)
			throws CustomerDetailsNotFoundException,
			BillingServicesDownException {
		Customer customer=daoServices.getCustomer(customerID);
		if(customer==null) throw new CustomerDetailsNotFoundException("Customer do not exists: "+customerID);
		return customer;
	}
	@Override
	public List<Customer> getAllCustomerDetails()throws BillingServicesDownException {
		return daoServices.getAllCustomers();
	}
	@Override
	public PostpaidAccount getPostPaidAccountDetails(int customerID,
			long mobileNo) throws CustomerDetailsNotFoundException,
			PostpaidAccountNotFoundException, BillingServicesDownException {
		this.getCustomerDetails(customerID);
		PostpaidAccount postpaidAccount=daoServices.getCustomerPostPaidAccount(customerID, mobileNo);
		if(postpaidAccount==null) throw new PostpaidAccountNotFoundException("Account for" +customerID+" do not exists");
		return postpaidAccount;
	}
	@Override
	public List<PostpaidAccount> getCustomerAllPostpaidAccountsDetails(
			int customerID) throws CustomerDetailsNotFoundException,
			BillingServicesDownException {
		this.getCustomerDetails(customerID);
		return daoServices.getCustomerPostPaidAccounts(customerID);
	}
	@Override
	public Bill getMobileBillDetails(int customerID, long mobileNo,
			String billMonth) throws CustomerDetailsNotFoundException,
			PostpaidAccountNotFoundException, InvalidBillMonthException,
			BillDetailsNotFoundException, BillingServicesDownException {
		this.getPostPaidAccountDetails(customerID, mobileNo);
		Bill bill=daoServices.getMonthlyBill(customerID, mobileNo, billMonth);
		if(bill==null)throw new BillDetailsNotFoundException("Bill details for"+ mobileNo+"not available");
		return bill;
	}
	@Override
	public List<Bill> getCustomerPostPaidAccountAllBillDetails(int customerID,
			long mobileNo) throws CustomerDetailsNotFoundException,
			PostpaidAccountNotFoundException, BillingServicesDownException, BillDetailsNotFoundException {
		this.getPostPaidAccountDetails(customerID, mobileNo);
		if(daoServices.getCustomerPostPaidAccount(customerID, mobileNo).getBills()==null) throw new BillDetailsNotFoundException("Bill details for"+mobileNo+"not available");
		return daoServices.getCustomerPostPaidAccountAllBills(customerID, mobileNo);
	}
	@Override
	public boolean changePlan(int customerID, long mobileNo, int planID)
			throws CustomerDetailsNotFoundException,
			PostpaidAccountNotFoundException, PlanDetailsNotFoundException,
			BillingServicesDownException {
		Plan plan = daoServices.getPlan(planID);
		if(plan==null) throw new PlanDetailsNotFoundException("Plan does not exist.");
		this.getPostPaidAccountDetails(customerID, mobileNo).setPlan(plan);
		daoServices.updatePostPaidAccount(customerID, new PostpaidAccount(mobileNo,plan));
		return true;
	}
	@Override
	public boolean closeCustomerPostPaidAccount(int customerID, long mobileNo)
			throws CustomerDetailsNotFoundException,
			PostpaidAccountNotFoundException, BillingServicesDownException {
		this.getPostPaidAccountDetails(customerID, mobileNo);
		return daoServices.deletePostPaidAccount(customerID, mobileNo);
	}
	@Override
	public boolean deleteCustomer(int customerID)
			throws BillingServicesDownException,
			CustomerDetailsNotFoundException {
		this.getCustomerDetails(customerID);
		return daoServices.deleteCustomer(customerID);
	}
	@Override
	public Plan getCustomerPostPaidAccountPlanDetails(int customerID,
			long mobileNo) throws CustomerDetailsNotFoundException,
			PostpaidAccountNotFoundException, BillingServicesDownException,
			PlanDetailsNotFoundException {
		this.getPostPaidAccountDetails(customerID, mobileNo);
		return daoServices.getCustomerPostPaidAccount(customerID, mobileNo).getPlan();
	}
}