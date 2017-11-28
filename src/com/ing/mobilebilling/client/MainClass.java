package com.ing.mobilebilling.client;
import java.util.Scanner;

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
import com.ing.mobilebilling.serviceprovider.ServiceProvider;
import com.ing.mobilebilling.services.BillingServices;
public class MainClass {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws BillingServicesDownException, CustomerDetailsNotFoundException, PlanDetailsNotFoundException, PostpaidAccountNotFoundException, InvalidBillMonthException, BillDetailsNotFoundException {
		
		 
		String firstName, lastName, emailID,billMonth, dateOfBirth,billingAddressCity,billingAddressState,homeAddressCity,homeAddressState;
		int homeAddressPinCode, billingAddressPinCode,customerID,planID,noOfLocalSMS,noOfStdSMS,noOfLocalCalls,noOfStdCalls,internetDataUsageUnits;
		BillingServices billingServices = ServiceProvider.getBillingServices();
		Scanner sc =new Scanner(System.in);	
		do{
			System.out.println("1.Create Customer\n2.Open Postpaid Account\n3.Get Customer Details\n4.Get All Customer Details\n5.Get Postpaid Account Details\n6.Get All Postpaid Account Details\n7.Generate Monthly Bill\n8.Get Mobile Bill Details for month\n9.Get Postpaid Accounts All Bill Details\n10.Get Postpaid Plan Details\n11.Change Postpaid plan\n12.Close postpaid Account n13.Delete Account\n14.Exit");
			int option = sc.nextInt();
			try{
			switch (option) {
			case 1:
						System.out.println("Enter your FirstName");  
							firstName=sc.next();   
						System.out.println("Enter your LastName");  
							lastName=sc.next();   
						System.out.println("Enter your Email ID");  
							emailID=sc.next();   
						System.out.println("Enter your Date of Birth");  
							dateOfBirth=sc.next();
						System.out.println("Enter your Billing address City");  
							billingAddressCity=sc.next();   
						System.out.println("Enter your Billing address State"); 
							billingAddressState=sc.next();   
						System.out.println("Enter your Billing address Pincode");  
							billingAddressPinCode=sc.nextInt();   
						System.out.println("Enter your Home address City");  
							homeAddressCity=sc.next();   
						System.out.println("Enter your Home address State"); 
							homeAddressState=sc.next();   
						System.out.println("Enter your Home address Pincode");  
							homeAddressPinCode=sc.nextInt();
						customerID = billingServices.acceptCustomerDetails(firstName, lastName, emailID, dateOfBirth, billingAddressCity, billingAddressState, billingAddressPinCode, homeAddressCity, homeAddressState, homeAddressPinCode);
						System.out.println("Your Customer Id is:"+customerID);
						break;
			case 2:
				System.out.println("Select the plan:");
				for (Plan plan : billingServices.getPlanAllDetails()) 
					if(plan!=null)
						System.out.println(plan.toString());
				System.out.println("Enter your customer Id");
						customerID = sc.nextInt();
				System.out.println("Enter your Plan Id");
						planID = sc.nextInt();
				long mobileNumber =billingServices.openPostpaidMobileAccount(customerID, planID);
					System.out.println("Your Mobile Number is: "+mobileNumber);
				break;
			case 3:
					System.out.println("Enter your Customer Id");
						customerID = sc.nextInt();
					System.out.println(billingServices.getCustomerDetails(customerID));
				    break;
			case 4:
					for (Customer customer : billingServices.getAllCustomerDetails())
						if(customer!=null)
							System.out.println(customer);
				   break;
			case 5:
				System.out.println("Enter your Customer Id");
					customerID = sc.nextInt();
				System.out.println("Enter your Mobile Number");
					mobileNumber = sc.nextInt();
				System.out.println(billingServices.getPostPaidAccountDetails(customerID, mobileNumber));
				break;
			case 6:
				System.out.println("Enter your Customer Id");
				customerID = sc.nextInt();
				for (PostpaidAccount account : billingServices.getCustomerAllPostpaidAccountsDetails(customerID)) 
					if(account!=null)
						System.out.println(account);
				break;
			case 7:
				System.out.println("Enter your Customer Id");
					customerID = sc.nextInt();
				System.out.println("Enter your Mobile Number");
					mobileNumber = sc.nextInt();
				System.out.println("Enter your Bill Month");  
					billMonth=sc.next();   
				System.out.println("Enter your Number of Local Calls");  
					noOfLocalCalls=sc.nextInt();   
				System.out.println("Enter your Number of Local SMS");  
					noOfLocalSMS=sc.nextInt(); 
				System.out.println("Enter your Number of STD Calls");  
					noOfStdCalls=sc.nextInt();   
				System.out.println("Enter your Number of STD SMS");  
					noOfStdSMS=sc.nextInt();
				System.out.println("Enter your Internet data usage");  
					internetDataUsageUnits=sc.nextInt();
				int monthTotalBill = billingServices.generateMonthlyMobileBill(customerID, mobileNumber, billMonth, noOfLocalSMS, noOfStdSMS, noOfLocalCalls, noOfStdCalls, internetDataUsageUnits);
				System.out.println("Your total bill amount  is: "+ monthTotalBill);
				break;
			case 8:
				System.out.println("Enter your Customer Id");
					customerID = sc.nextInt();
				System.out.println("Enter your Mobile Number");
					mobileNumber = sc.nextInt();
				System.out.println("Enter Month to get Bill.");
					billMonth = sc.next();
				System.out.println(billingServices.getMobileBillDetails(customerID, mobileNumber, billMonth));
				break;
			case 9:
				System.out.println("Enter your Customer Id");
					customerID = sc.nextInt();
				System.out.println("Enter your Mobile Number");
					mobileNumber = sc.nextInt();
				for (Bill bill : billingServices.getCustomerPostPaidAccountAllBillDetails(customerID, mobileNumber))
						if(bill!=null)
							System.out.println(bill);
				break;
			case 10:
				System.out.println("Enter your Customer Id");
					customerID = sc.nextInt();
				System.out.println("Enter your Mobile Number");
					mobileNumber = sc.nextInt();
				System.out.println(billingServices.getCustomerPostPaidAccountPlanDetails(customerID, mobileNumber));
				break;
			case 11:
				System.out.println("Select the plan....");
				for (Plan plan : billingServices.getPlanAllDetails()) 
				if(plan!=null)
					System.out.println(plan.toString());
				System.out.println("Enter your Customer Id");
					customerID = sc.nextInt();
				System.out.println("Enter your Mobile Number");
					mobileNumber = sc.nextInt();
				System.out.println("Enter your Plan Id");
					int planId = sc.nextInt();
					billingServices.changePlan(customerID, mobileNumber, planId);
					System.out.println("Your plan has been changed");
					System.out.println(billingServices.getCustomerPostPaidAccountPlanDetails(customerID, mobileNumber));
				break;
			case 12:
				System.out.println("Enter your Customer Id");
					customerID = sc.nextInt();
				System.out.println("Enter your Mobile Number");
				mobileNumber = sc.nextInt();
				if(billingServices.closeCustomerPostPaidAccount(customerID, mobileNumber)==true);
				System.out.println("Your Postpaid account has been deactivated");
				break;
			case 13:
				System.out.println("Enter your Customer Id");
					customerID = sc.nextInt();
				billingServices.deleteCustomer(customerID);
				System.out.println("Your account/s has been deactivated");
				break;
			case 14:
				System.out.println("Exit....");
				System.exit(0);
				break;				
			default:
				System.out.println("Please choose correct option.");
				break;
			}
		}
			catch(BillDetailsNotFoundException bill){
				System.out.println(bill.getMessage());
			}
			catch(PlanDetailsNotFoundException plan){
				System.out.println(plan.getMessage());
			}
			catch(PostpaidAccountNotFoundException account){
				System.out.println(account.getMessage());
			}
			catch(CustomerDetailsNotFoundException customer){
				System.out.println(customer.getMessage());
			}
			catch(BillingServicesDownException billing){
				System.out.println(billing.getMessage());
			}
		}while(true);
	}
}