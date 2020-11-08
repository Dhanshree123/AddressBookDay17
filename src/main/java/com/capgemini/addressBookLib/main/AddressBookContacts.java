package com.capgemini.addressBookLib.main;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByName;

public class AddressBookContacts {
	@CsvBindByName
	public String firstName;

	@CsvBindByName
	public String lastName;

	@CsvBindByName
	public String address;

	@CsvBindByName
	public String city;

	@CsvBindByName
	public String state;

	@CsvBindByName
	public int zip;

	@CsvBindByName
	public String phoneNumber;

	@CsvBindByName
	public String email;

	public String addressBookName;
	public String addressBookType;

	public LocalDate addedDate;

	public AddressBookContacts() {

	}

	public AddressBookContacts(String firstName, String lastName, String address, String city, String state, int zip,
			String phoneNumber, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;

	}

	public AddressBookContacts(String firstName, String lastName, String address, String city, String state, int zip,
			String phoneNumber, String email, String addressBookName, String addressBookType) {
		this(firstName, lastName, address, city, state, zip, phoneNumber, email);
		this.addressBookName = addressBookName;
		this.addressBookType = addressBookType;

	}

	public AddressBookContacts(String firstName, String lastName, String address, String city, String state, int zip,
			String phoneNumber, String email, LocalDate addedDate) {
		this(firstName, lastName, address, city, state, zip, phoneNumber, email);
		this.addedDate = addedDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getaddress() {
		return address;
	}

	public void setaddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddressBookName() {
		return addressBookName;
	}

	public void setAddressBookName(String addressBookName) {
		this.addressBookName = addressBookName;
	}

	public String getAddressBookType() {
		return addressBookType;
	}

	public void setAddressBookType(String addressBookType) {
		this.addressBookType = addressBookType;
	}

	public boolean equals(Object obj) {

		if (obj == this)
			return true;

		if (getClass() != obj.getClass())
			return false;

		AddressBookContacts c = (AddressBookContacts) obj;
		if (this.firstName.equals(c.firstName))
			return true;

		else
			return false;

	}

	public String toString() {
		return "First name : " + firstName + " ,Last name : " + lastName + " ,Address : " + address + " ,City : " + city
				+ " ,State : " + state + " ,Zip : " + zip + " ,Phone num : " + phoneNumber + " ,Email : " + email;
	}
}
