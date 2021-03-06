package com.capgemini.addressBookLib;

import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.addressBookLib.exception.AddressBookException;
import com.capgemini.addressBookLib.main.AddressBook;
import com.capgemini.addressBookLib.main.AddressBookContacts;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookTest {

	@Before
	public void setUp() throws IOException {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public AddressBookContacts[] getContactList() {
		Response response = RestAssured.get("/contact");
		System.out.println("Jsonserver Data:\n" + response.asString());
		AddressBookContacts[] contacts = new Gson().fromJson(response.asString(), AddressBookContacts[].class);
		return contacts;
	}

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

	@Test
	public void givenManyEmployees_WhenAddedToDatabaseWithThreads_ShouldSyncWithDB() {
		AddressBook addressBook = new AddressBook();
		AddressBookContacts[] contacts = {
				new AddressBookContacts("A", "B", "Street1", "ca", "sa", 444444, "9098979695", "ab@gmail.com",
						LocalDate.parse("2019-01-02")),
				new AddressBookContacts("C", "D", "Street2", "ca", "sa", 444444, "8898979695", "cd@gmail.com",
						LocalDate.parse("2019-01-03")) };
		addressBook.addMultipleAddressContacts(Arrays.asList(contacts));
		long entries = addressBook.readAddressBookData().size();
		Assert.assertEquals(9, entries);
	}

	@Test
	public void givenContactDataInJsonServer_whenRetreived_shouldMatchCount() throws IOException {
		AddressBookContacts[] contactList = getContactList();
		AddressBook addressBook = new AddressBook(Arrays.asList(contactList));
		int entries = addressBook.countEntries();
		Assert.assertEquals(11, entries);
	}

	@Test
	public void givenMultipleContactData_WhenAddedToJsonServer_ShouldMatchSize() throws IOException {
		AddressBookContacts[] contactsArray = getContactList();
		AddressBook addressBook = new AddressBook(Arrays.asList(contactsArray));
		AddressBookContacts[] contacts = {
				new AddressBookContacts("A", "B", "Street1", "ca", "sa", 444444, "9098979695", "ab@gmail.com",
						LocalDate.parse("2019-01-02")),
				new AddressBookContacts("C", "D", "Street2", "ca", "sa", 444444, "8898979695", "cd@gmail.com",
						LocalDate.parse("2019-01-03")) };
		for (int i = 0; i < contacts.length; i++) {
			Response response = addContactToJsonServer(contacts[i]);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);

			contacts[i] = new Gson().fromJson(response.asString(), AddressBookContacts.class);
			addressBook.addContact(contacts[i]);
		}
		long entries = addressBook.countEntries();
		Assert.assertEquals(19, entries);
	}

	private Response addContactToJsonServer(AddressBookContacts contact) {
		String jsonContact = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(jsonContact);
		return request.post("/contact");
	}

	@Test
	public void givenNewPhoneNumberForContact_WhenUpdated_ShouldMatch200Response() throws IOException {
		AddressBookContacts[] contactsArray = getContactList();
		AddressBook addressBook = new AddressBook(Arrays.asList(contactsArray));
		addressBook.updateContactPhoneNumber("A", "B", "9098979699");
		AddressBookContacts contact = addressBook.getContact("A", "B");
		String contactJson = new Gson().toJson(contact);

		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		Response response = request.put("/contact/" + contact.phoneNumber);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}

	@Test
	public void givenContactToDelete_WhenDeleted_ShouldMatch200ResponseAndCount() throws IOException {
		AddressBookContacts[] contactsArray = getContactList();
		List<AddressBookContacts> contactList = new ArrayList<AddressBookContacts>(Arrays.asList(contactsArray));
		AddressBook addressBook = new AddressBook(contactList);
		AddressBookContacts contact = addressBook.getContact("A", "B");
		String contactJson = new Gson().toJson(contact);

		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		Response response = request.delete("/contact/" + contact.firstName);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		long entries = addressBook.countEntries();
		Assert.assertEquals(17, entries);
	}

}