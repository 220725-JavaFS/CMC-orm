package com.revature.models;

import java.sql.Connection;
import java.sql.SQLException;

import com.revature.utils.ConnectionUtil;

public class CustomORM {

//	will i implement my code into methods here? or break this out further into more classes

	private String connectionString;
	private String connectionUsername;
	private String connectionPassword;
	private String driverName;

//	This probably shouldn't be callable
//	public customORM() {
//		System.out.println("Default Constructor won't work");
//	}

	public CustomORM(String connectionString, String connectionUsername, String connectionPassword, String driverName) {
		super();
		this.connectionString = connectionString;
		this.connectionUsername = connectionUsername;
		this.connectionPassword = connectionPassword;
		this.driverName = driverName;
	}

	// returns my connection object, or maybe should set it as a instance variable
	public Connection getConnection() {
		try (Connection conn = ConnectionUtil.getConnection(connectionString, connectionUsername, connectionPassword,
				driverName)) {
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		CustomORM customORM = new CustomORM(
				"jdbc:postgresql://javafs220725.cgbxdd5yszbt.us-east-2.rds.amazonaws.com:5432/project0", "postgres",
				"password", "org.postgresql.Driver");
		
		Connection connection = customORM.getConnection();
		if (connection!=null) {
			System.out.println("Connection to DB succesful");
		}
	}

}
