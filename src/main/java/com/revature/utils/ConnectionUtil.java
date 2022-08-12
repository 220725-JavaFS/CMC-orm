package com.revature.utils;

import java.sql.Connection; //java.sql is the JDBC package
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	// A singleton design pattern only allows one instance of a Class to exist in
	// memory at a time.
	// connection here is a singleton

	private static Connection connection;

	public static Connection getConnection(String connectionString, String connectionUsername,
			String connectionPassword, String driverName) throws SQLException {
		if (connection != null && !connection.isClosed()) {
			return connection;
		} else {

			// For many frameworks, or in cases where there are multiple SQL drivers, you
			// will need to register which
			// Driver you are using for the connection interface. The Class.forName method
			// will allow you to do this.
			// This step is often unnecessary for simple projects but is considered best
			// practice.

			try {
				Class.forName(driverName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			String url = connectionString;
			String username = connectionUsername; // It is possible to hide raw credentials by using ENV variables
			String password = connectionPassword; // You can access those variables with System.getenv("var-name");

			connection = DriverManager.getConnection(url, username, password);

			return connection;

		}
	}
}