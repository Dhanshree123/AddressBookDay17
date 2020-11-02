package com.capgemini.addressBookLib;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

}
