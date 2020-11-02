package com.capgemini.addressBookLib;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AddressBookDBService {
	private static AddressBookDBService addressBookDBService = null;

	private AddressBookDBService() {

	}

	public static AddressBookDBService getInstance() {
		if (addressBookDBService == null)
			addressBookDBService = new AddressBookDBService();
		return addressBookDBService;

	}

	public List<AddressBookContacts> readData() {
		String sql = "SELECT * FROM addressBook,contact WHERE addressBook.contactid = contact.contactid ;";
		List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int id = resultSet.getInt("contactid");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String address = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				int zip = resultSet.getInt("zip");
				String ph_no = resultSet.getString("phoneNumber");
				String email = resultSet.getString("email");
				String addressBookName = resultSet.getString("addressBookName");
				String addressBookType = resultSet.getString("addressBookType");
				addressBookContactsList.add(new AddressBookContacts(firstName, lastName, address, city, state, zip,
						ph_no, email, addressBookName, addressBookType));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressBookContactsList;
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/addressBook_system?useSSL=false";
		String userName = "root";
		String password = "Dhpatil@23";
		Connection con;
		System.out.println("Connecting to database:" + jdbcURL);
		con = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is successful!!" + con);
		return con;
	}

	public List<AddressBookContacts> getContactData(String firstname) {
		try (Connection connection = this.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
					"select * from contact,addressBook where firstName=? and addressBook.contactid = contact.contactid;");
			preparedStatement.setString(1, firstname);
			ResultSet resultSet = preparedStatement.executeQuery();
			return getContactData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<AddressBookContacts> getContactData(ResultSet resultSet) {
		List<AddressBookContacts> contactList = new ArrayList<>();

		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String address = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				int zip = resultSet.getInt("zip");
				String phoneNumber = resultSet.getString("phoneNumber");
				String email = resultSet.getString("email");
				String addressBookName = resultSet.getString("addressBookName");
				String addressBookType = resultSet.getString("addressBookType");
				AddressBookContacts contact = new AddressBookContacts(firstName, lastName, address, city, state, zip,
						phoneNumber, email, addressBookName, addressBookType);
				contactList.add(contact);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;

	}

	public int updateAddressBook(String firstName, String ph_no) throws AddressBookException {
		return this.updateAddressBookDataUsingPreparedStatement(firstName, ph_no);
	}

	private int updateAddressBookDataUsingPreparedStatement(String firstName, String ph_no)
			throws AddressBookException {
		try {
			Connection connection = this.getConnection();
			String sql = String.format("UPDATE contact SET phoneNumber ='%s' WHERE firstname='%s' ;", firstName, ph_no);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			return preparedStatement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddressBookException("unable to create prepared statement");
		}
	}

	public List<AddressBookContacts> getContactInRange(LocalDate startDate, LocalDate endDate) {
		String sql = String.format("SELECT * FROM contact WHERE dateAdded BETWEEN '%s' and '%s';",
				Date.valueOf(startDate), Date.valueOf(endDate));
		List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int id = resultSet.getInt("contactid");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String address = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				int zip = resultSet.getInt("zip");
				String ph_no = resultSet.getString("phoneNumber");
				String email = resultSet.getString("email");
				LocalDate addedDate = resultSet.getDate("dateAdded").toLocalDate();
				addressBookContactsList.add(new AddressBookContacts(firstName, lastName, address, city, state, zip,
						ph_no, email, addedDate));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressBookContactsList;
	}

	public int getContactByCity(String city) {
		String sql = String.format("SELECT * FROM contact WHERE city = '%s';", city);
		int count = 0;
		List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getContactByState(String state) {
		String sql = String.format("SELECT * FROM contact WHERE state = '%s';", state);
		int count = 0;
		List<AddressBookContacts> addressBookContactsList = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public void addContact(String firstName, String lastName, String address, String city, String state, int zip,
			String phone, String email, String addressBookName, String addressBookType) {
		String sql = String.format(
				"insert into contact(firstName,lastName,address,city,state,zip,phoneNumber,email)"
						+ "values ('%s','%s','%s','%s','%s','%d','%s','%s')",
				firstName, lastName, address, city, state, zip, phone, email);

		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sqlNext = String.format(
				"insert into addressBook(AddressBookType,AddressBookName)" + "values ('%s','%s')", addressBookType,
				addressBookName);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(sqlNext, statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
