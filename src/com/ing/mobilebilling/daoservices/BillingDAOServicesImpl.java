package com.ing.mobilebilling.daoservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ing.mobilebilling.beans.Bill;
import com.ing.mobilebilling.beans.Customer;
import com.ing.mobilebilling.beans.Plan;
import com.ing.mobilebilling.beans.PostpaidAccount;
import com.ing.mobilebilling.exceptions.BillingServicesDownException;
import com.ing.mobilebilling.exceptions.PlanDetailsNotFoundException;


public class BillingDAOServicesImpl implements BillingDAOServices {
		
	private static int CUSTOMER_ID_COUNTER=1001,BILL_ID_COUNTER=1000;
	private static long MOBILE_NUMBER_COUNTER=954590000l;
	private static HashMap<Integer, Customer> customers = new HashMap<>();
	private static HashMap<Integer, Plan> plans = new HashMap<>();
	static {
		plans.put(1, new Plan(1, 250, 20, 25, 85, 45, 1500, 1, 2.5f, 1, 3, 0.25f, "Indore-MP", "VodaA"));
		plans.put(2, new Plan(2, 400, 30, 20, 70, 60, 3500, 1, 1.5f, 1, 2.0f, 0.40f, "Mumbai-Maha", "Voda B"));
		plans.put(3, new Plan(3, 550, 45, 30, 65, 50, 2500, 0.5f, 1.0f, 1.6f, 2.0f, 0.15f, "Kolkata-WB", "VodaC"));
		plans.put(4, new Plan(4, 700, 50, 40, 90, 30, 8000, 0.5f, 1.0f, 1, 1.5f, 0.30f, "Jaipur-Rajsthan", "Voda D"));
	}
	@Override
	public int insertCustomer(Customer customer)
			throws BillingServicesDownException {
		customer.setCustomerID(CUSTOMER_ID_COUNTER++);
		customers.put(customer.getCustomerID(), customer);
		return customer.getCustomerID();
	}
	@Override
	public long insertPostPaidAccount(int customerID, PostpaidAccount account) {
		account.setMobileNo(MOBILE_NUMBER_COUNTER++);
		this.getCustomer(customerID).setPostpaidAccounts(account.getMobileNo(),account);
		return account.getMobileNo() ;
	}
	@Override
	public boolean updatePostPaidAccount(int customerID, PostpaidAccount account) {
		this.insertPostPaidAccount(customerID, account);
		return true;
	}
	@Override
	public int insertMonthlybill(int customerID, long mobileNo, Bill bill) {
		bill.setBillID(BILL_ID_COUNTER++);
		getCustomerPostPaidAccount(customerID, mobileNo).setBills(bill.getBillID(), bill);;
		return (int)bill.getTotalBillAmount() ;
	}

	@Override
	public int insertPlan(Plan plan) throws PlanDetailsNotFoundException {
		return 0;
	}
	@Override
	public boolean deletePostPaidAccount(int customerID, long mobileNo) {
			getCustomer(customerID).getPostpaidAccounts().remove(mobileNo);
		return true;
	}
	@Override
	public Bill getMonthlyBill(int customerID, long mobileNo, String billMonth) {
		for (Bill bill : getCustomerPostPaidAccountAllBills(customerID, mobileNo)) 
			if(bill!=null && bill.getBillMonth().equals(billMonth))
				return bill;
		return null;
	}
	@Override
	public List<Bill> getCustomerPostPaidAccountAllBills(int customerID,long mobileNo) {
		return new ArrayList<>(this.getCustomerPostPaidAccount(customerID, mobileNo).getBills().values());
	}
	@Override
	public List<PostpaidAccount> getCustomerPostPaidAccounts(int customerID) {
		return new ArrayList<>(getCustomer(customerID).getPostpaidAccounts().values());
	}
	@Override
	public Customer getCustomer(int customerID) {
		return customers.get(customerID);
	}
	@Override
	public List<Customer> getAllCustomers() {
		return new ArrayList<>(customers.values());
	}
	@Override
	public List<Plan> getAllPlans() {
		return new ArrayList<>(plans.values());
	}
	@Override
	public Plan getPlan(int planID) {
		return plans.get(planID);
	}
	@Override
	public PostpaidAccount getCustomerPostPaidAccount(int customerID,
			long mobileNo) {
			for(PostpaidAccount postpaidAccount : this.getCustomerPostPaidAccounts(customerID))
				if(postpaidAccount!=null && postpaidAccount.getMobileNo()==mobileNo)
			return postpaidAccount;
	return null;
	}
	@Override
	public Plan getPlanDetails(int customerID, long mobileNo) {
		for(PostpaidAccount postpaidAccount : this.getCustomerPostPaidAccounts(customerID))
			if(postpaidAccount!=null && postpaidAccount.getMobileNo()==mobileNo)
				return getCustomerPostPaidAccount(customerID, mobileNo).getPlan();
		return null;
	}
	@Override
	public boolean deleteCustomer(int customerID) {
		customers.remove(customerID);
		return true;
	}	
}