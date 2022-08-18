package com.revature;

import java.sql.Connection;
import java.sql.SQLException;

import com.revature.utils.ConnectionUtil;

public class CustomORM {

//	will i implement my code into methods here? or break this out further into more classes

	private String connectionString;
	private String connectionUsername;
	private String connectionPassword;
	private String driverName;
	private String databaseName;

//	This probably shouldn't be callable
	

	public CustomORM(String connectionString, String connectionUsername, String connectionPassword, String driverName, String databaseName) {
		super();
		this.connectionString = connectionString;
		this.connectionUsername = connectionUsername;
		this.connectionPassword = connectionPassword;
		this.driverName = driverName;
		this.setDatabaseName(databaseName);
	}
	
//	public boolean createTable(String tableName) {
//		//Check if table exists.
//		//If it does exist print an error to the return 
//		
//	}

	public CustomORM() {
		super();
	}

	// returns my connection object, or maybe should set it as a instance variable
	public Connection getConnection() {
		try (Connection conn = ConnectionUtil.getConnection(connectionString, connectionUsername, connectionPassword,
				driverName, databaseName)) {
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean createDB(String db) {
		String sql = "";
	}

	public static void main(String[] args) {
		CustomORM customORM = new CustomORM(
				"jdbc:postgresql://javafs220725.cgbxdd5yszbt.us-east-2.rds.amazonaws.com:5432/", "postgres",
				"password", "org.postgresql.Driver", "project1");
		
		Connection connection = customORM.getConnection();
		if (connection!=null) {
			System.out.println("Connection to DB succesful");
		} else {
			System.out.println("Connection unsuccesful");
		}
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public void setConnectionUsername(String connectionUsername) {
		this.connectionUsername = connectionUsername;
	}

	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

}
