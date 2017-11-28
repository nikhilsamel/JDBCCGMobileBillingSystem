package com.ing.mobilebilling.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ing.mobilebilling.beans.Address;
import com.ing.mobilebilling.beans.Bill;
import com.ing.mobilebilling.beans.Customer;
import com.ing.mobilebilling.beans.Plan;
import com.ing.mobilebilling.beans.PostpaidAccount;
import com.ing.mobilebilling.daoservices.BillingDAOServices;
import com.ing.mobilebilling.services.BillingServices;
import com.ing.mobilebilling.services.BillingServicesImpl;





public class MobileBillingServicesTestUsingMokito {

	private static BillingServices billingServices;
	private static BillingDAOServices mockDaoServices;

	@BeforeClass
	public static void setUpBillingServices() {
		mockDaoServices = Mockito.mock(BillingDAOServices.class);
		billingServices = new BillingServicesImpl(mockDaoServices);
	}

	
	@Before
	public void setUpTestData() throws SQLException {
		
		Address address1 = new Address(35000, "Pune", "Maharashtra");
		Address address2 = new Address(35000, "Pune", "Maharashtra");
		Plan plan1 = new Plan(35000, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, "Pune", "Pune");
		Bill bill1 = new Bill(35000, 43, 43, 43, 43, 43, "November", 43, 43, 43, 43, 43, 43, 43, 43);
		Bill bill2 = new Bill(35000, 43, 43, 43, 43, 43, "December", 43, 43, 43, 43, 43, 43, 43, 43);
		HashMap<Integer, Bill> bills1 = new HashMap<Integer, Bill>();
		bills1.put(1, bill1);
		bills1.put(2, bill2);
		PostpaidAccount postpaidAccount1 = new PostpaidAccount(1234567891, plan1, bills1);
		PostpaidAccount postpaidAccount2 = new PostpaidAccount(1234567891, plan1, bills1);
		HashMap<Long, PostpaidAccount> postpaidAccounts1 = new HashMap<Long, PostpaidAccount>();
		postpaidAccounts1.put(1l, postpaidAccount1);
		postpaidAccounts1.put(2l, postpaidAccount2);
		Customer customer1 = new Customer(1000, "Satish", "Mahajan", "satish.mahajan@capgemini.com", "19/11/2017", address1, address2);
		
		Address address3 = new Address(35000, "Pune", "Maharashtra");
		Address address4 = new Address(35000, "Pune", "Maharashtra");
		Plan plan3 = new Plan(35000, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, "Pune", "Pune");
		Bill bill3 = new Bill(35000, 43, 43, 43, 43, 43, "November", 43, 43, 43, 43, 43, 43, 43, 43);
		Bill bill4 = new Bill(35000, 43, 43, 43, 43, 43, "December", 43, 43, 43, 43, 43, 43, 43, 43);
		HashMap<Integer, Bill> bills2 = new HashMap<Integer, Bill>();
		bills2.put(1, bill3);
		bills2.put(2, bill4);
		PostpaidAccount postpaidAccount3 = new PostpaidAccount(1234567891, plan1, bills2);
		PostpaidAccount postpaidAccount4 = new PostpaidAccount(1234567891, plan1, bills2);
		HashMap<Long, PostpaidAccount> postpaidAccounts2 = new HashMap<Long, PostpaidAccount>();
		postpaidAccounts2.put(1l, postpaidAccount3);
		postpaidAccounts2.put(2l, postpaidAccount4);
		Customer customer2 = new Customer(1001, "Satish", "Mahajan", "satish.mahajan@capgemini.com", "19/11/2017", address3, address4);
		
		Associate associate2 =new Associate(1001, 87372, "Ayush", "Mahajan", "Training", "Manager", "YCTCC727",
				"ayush.mahajan@capgemini.com", new Salary(87738, 1800, 1800),
				new BankDetails(12345, "HDFC", "HDFC0097"));
		ArrayList<Associate> associatesList = new ArrayList<>();
		associatesList.add(associate1);
		associatesList.add(associate2);
		Associate associate3 =new Associate( 65440, "Mayur", "Patil", "ADC", "Trainee", "CYYJUUF887",
				"mayur.patil@capgemini.com", new Salary(86222, 1800, 1800),
				new BankDetails(123738, "HDFC", "HDFC0097"));
		
		Mockito.when(mockDaoServices.getAssociate(1000)).thenReturn(associate1);
		Mockito.when(mockDaoServices.getAssociate(1001)).thenReturn(associate2);
		Mockito.when(mockDaoServices.getAssociate(1234)).thenReturn(null);
		Mockito.when(mockDaoServices.getAssociates()).thenReturn(associatesList);
		Mockito.when(mockDaoServices.insertAssociate(associate3)).thenReturn(1002);
		
		
	}

	@Test(expected = AssociateDetailsNotFoundException.class)
	public void testGetAssociateDataForInvalidAssociateId()
			throws PayrollServicesDownException, AssociateDetailsNotFoundException {
		payrollServices.getAssociateDetails(1234);
	}

	@Test
	public void testGetAssociateDataForValidAssociateId()
			throws PayrollServicesDownException, AssociateDetailsNotFoundException, SQLException {
		Associate expectedAssociate = new Associate(1000, 78000, "Satish", "Mahajan", "Training", "Manager", "DTDYF736",
				"satish.mahajan@capgemini.com", new Salary(35000, 1800, 1800),
				new BankDetails(12345, "HDFC", "HDFC0097"));
		Associate actualAssociate = payrollServices.getAssociateDetails(1000);
		
		Mockito.verify(mockDaoServices).getAssociate(Mockito.anyInt());
		assertEquals(expectedAssociate, actualAssociate);
	}

	@Test
	public void testAcceptAssociateDetailsForValidData() throws PayrollServicesDownException {
		int expectedAssociateId = 1002;
		int actualAssociateId = payrollServices.acceptAssociateDetails("NIlesh", "Patil", "nilesh.patil@capgemini.com",
				"ADM", "Manager", "ohhoh73763", 78999, 30000, 1800, 1800, 645454, "ICICI", "ICICI8437");
		assertEquals(expectedAssociateId, actualAssociateId);
	}

	@Test(expected = AssociateDetailsNotFoundException.class)
	public void testCalculateNetSalaryForInvalidAssociateId()
			throws PayrollServicesDownException, AssociateDetailsNotFoundException {
		payrollServices.getAssociateDetails(63363);
	}

	@Test
	public void testCalculateNetSalaryForValidAssociateId() {
		fail();
	}

	@Test
	public void testGetAllAssociatesDetails() throws PayrollServicesDownException {
		List<Associate> expectedAssociateList = new ArrayList<>(PayrollDAOServicesImpl.associates.values());
		List<Associate> actualAssociateList = payrollServices.getAllAssociatesDetails();
		assertEquals(expectedAssociateList, actualAssociateList);
	}

	@After
	public void tearDownTestData() {
		
	}

	@AfterClass
	public static void tearDownBillingServicesq() {
		mockDaoServices = null;
		billingServices = null;
	}

}
