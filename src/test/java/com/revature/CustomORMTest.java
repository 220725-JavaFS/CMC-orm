package com.revature;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CustomORMTest {

	private static String connectionString = "jdbc:postgresql://javafs220725.cgbxdd5yszbt.us-east-2.rds.amazonaws.com:5432/";
	private static String connectionUsername = "postgres";
	private static String connectionPassword = "password";
	private static String driverName = "org.postgresql.Driver";
	private static String databaseName = "project1";
	private static CustomORM customORM;
	
	@BeforeAll
	public static void createORM() {
		System.out.println("Initialize a connection to RDS without a DB");
		CustomORM customORM = new CustomORM();
		customORM.setConnectionString(connectionString);
		customORM.setConnectionUsername(connectionUsername);
		customORM.setConnectionPassword(connectionPassword);
		customORM.setDriverName(driverName);
		customORM.setDatabaseName(databaseName);
	}
	
	@Test
	public void createDB() {
		
	}
	
	
}
