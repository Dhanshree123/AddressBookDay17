package com.capgemini.addressBookLib;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

public class AddressBookTest {

	@Test
	public void givenAddressBookInDB_whenRetrieved_ShouldMatchEmployeeCount() {
		AddressBook addressBook = new AddressBook();
		long entries = addressBook.readAddressBookData().size();
		System.out.println("Number of entries:- " + entries);
		Assert.assertEquals(1, entries);
	}

	@Test
	public void givenContactName_WhenUpdated_ShouldSyncWithDB() throws AddressBookException {
		AddressBook addressBook = new AddressBook();
		addressBook.updateContact("Mini", "9876543210");
		boolean ans = addressBook.checkAddressBookDataInSyncWithDB("Mini");
		Assert.assertTrue(ans);
	}

	@Test
	public void givenAddressBookInDB_whenRetrievedForDateRange_ShouldMatchEmployeeCount() {
		AddressBook addressBook = new AddressBook();
		long entries = addressBook.readAddressBookData().size();
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<AddressBookContacts> list = addressBook.getInDateRange(startDate, endDate);
		System.out.println("Number of entries:- " + list.size());
		Assert.assertEquals(4, list.size());
	}

	@Test
	public void givenCityAndState_whenRetrieved_ShouldMatchEmployeeCount() {
		AddressBook addressBook = new AddressBook();
		int cityCount = addressBook.getContactsByCity("c");
		int stateCount = addressBook.getContactsByState("s");
		Assert.assertEquals(5, cityCount);
		Assert.assertEquals(5, stateCount);
	}

	@Test
	public void givenData_whenAdded_ShouldMatchEmployeeCount() {
		AddressBook addressBook = new AddressBook();
		addressBook.readAddressBookData();
		addressBook.addContactToAddressBook("Mini", "Kumar", "a", "c", "s", 123456, "9090909090", "kk@gmail.com",
				"office", "office1");
		boolean result = addressBook.checkAddressBookDataInSyncWithDB("Mini");
		Assert.assertTrue(result);
	}
}
