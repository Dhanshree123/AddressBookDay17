package com.capgemini.addressBookLib;

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
		addressBook.readAddressBookData();
		addressBook.updateContact("Mini", "9876543210");
		boolean ans = addressBook.checkAddressBookDataInSyncWithDB("Mini");
		Assert.assertTrue(ans);
	}

}
