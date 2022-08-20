package com.revature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.revature.utils.ConnectionUtil;

public class CustomORM {

//	will i implement my code into methods here? or break this out further into more classes

	private String connectionString;
	private String connectionUsername;
	private String connectionPassword;
	private String driverName;
	private String tableName;

//	This probably shouldn't be callable

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public CustomORM(String connectionString, String connectionUsername, String connectionPassword, String driverName,
			String tableName) {
		super();
		this.connectionString = connectionString;
		this.connectionUsername = connectionUsername;
		this.connectionPassword = connectionPassword;
		this.driverName = driverName;
		this.tableName = tableName;
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

	private Object convertStringToFieldType(String input, Class<?> type)
			throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
        switch(type.getName()){
        case "byte":
            return Byte.valueOf(input);
        case "short":
            return Short.valueOf(input);
        case "int":
            return Integer.valueOf(input);
        case "long":
            return Long.valueOf(input);
        case "double":
        	return Double.valueOf(input);
        case "float":
        	return Float.valueOf(input);
        case "boolean":
            return Boolean.valueOf(input);
        case "java.lang.String":
            return input;
        case "java.time.LocalDate":
            return LocalDate.parse(input);
        default:
            return type.getDeclaredConstructor().newInstance();
    }
	}

	public static void persistToDB(Object object, String tableName) {
		Class<?> c = object.getClass();
		Field[] fields = c.getDeclaredFields();
		for (int i = 1; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			
			String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			try {
				// obtain the getter method from the class we are mapping
				Method getterMethod = c.getMethod(getterName);
				// invoke that method on the object that we are mapping to
				Object fieldValue = getterMethod.invoke(object);
				System.out.println(field.getName());
				System.out.println(fieldValue.getClass());
				System.out.println(fieldValue);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		String sql = "INSERT INTO " + tableName + "(";
	}

	public static void main(String[] args) {
		// Establish ORM connection
		CustomORM customORM = new CustomORM(
				"jdbc:postgresql://javafs220725.cgbxdd5yszbt.us-east-2.rds.amazonaws.com:5432/movies", "postgres",
				"password", "org.postgresql.Driver", "movies");
		// get my connection object
		Connection connection = customORM.getConnection();

		Movie movie = new Movie();
		movie.setTitle("Black Widow");
		movie.setDescription("Black widow saves her family and saves the world");
		movie.setGenre("Action");
		movie.setImage_url(
				"https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQbLElYghjgbKOJxXIS89_bIWXV3RtB61uG3YRPopoXmeD0rJKf");

		persistToDB(movie, customORM.getTableName());

	}

}
