package com.revature;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import com.revature.models.Actor;
import com.revature.models.Movie;
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

	public String getConnectionString() {
		return connectionString;
	}

	public String getConnectionUsername() {
		return connectionUsername;
	}

	public String getConnectionPassword() {
		return connectionPassword;
	}

	public String getDriverName() {
		return driverName;
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
	public Connection getMyConnection(String connectionString, String connectionUsername, String connectionPassword,
			String driverName) {
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
		switch (type.getName()) {
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

	public LinkedList<Object> getJoin(Object object_1, Object object_2) {
		LinkedList<Object> list = new LinkedList<Object>();
		String table_1 = object_1.getClass().getSimpleName();
		String table_2 = object_2.getClass().getSimpleName();
		table_1 = table_1.substring(0, 1).toLowerCase() + table_1.substring(1);
		table_2 = table_2.substring(0, 1).toLowerCase() + table_2.substring(1);
		Class<?> c_1 = object_1.getClass();
		Field[] fields_1 = c_1.getDeclaredFields();
		Class<?> c_2 = object_2.getClass();
		Field[] fields_2 = c_2.getDeclaredFields();
		String getID_1 = fields_1[0].getName();
		String getID_2 = fields_2[0].getName();
		String ID_1 = getID_1.substring(0, 1).toUpperCase() + getID_1.substring(1);
		getID_1 = "get" + ID_1;
		String ID_2 = getID_2.substring(0, 1).toUpperCase() + getID_2.substring(1);
		getID_2 = "get" + ID_2;
		try {
			Method getterID_1 = c_1.getMethod(getID_1);
			Constructor<?> constructor = c_2.getConstructor();
			Method getterID_2 = c_2.getMethod(getID_2);
			Object ID_1_Value = getterID_1.invoke(object_1);
			Object ID_2_Value = getterID_2.invoke(object_2);
			StringBuilder builder = new StringBuilder(
					"SELECT * FROM " + table_1 + "_" + table_2 + " AS m_a JOIN " + table_2 + "s AS a ON m_a." + table_2
							+ "_id = a." + table_2 + "_id WHERE m_a." + table_1 + "_id = " + ID_1_Value + ";");
			Connection connection = ConnectionUtil.getConnection(connectionString, connectionUsername,
					connectionPassword, driverName);
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(builder.toString());
			while (result.next()) {
				Object new_object = constructor.newInstance();
				Field[] fields = c_2.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					String setterName = "set" + fields[i].getName().substring(0, 1).toUpperCase()
							+ fields[i].getName().substring(1);
//					System.out.println(fields[i].getGenericType().toString());
//					System.out.println(fields[i].getClass().getTypeName().toString());
//					System.out.println(setterName);
//					// check type of field as a string
					if (fields[i].getGenericType().toString().equals("class java.lang.String")) {
						Method setterMethod = c_2.getMethod(setterName, String.class);
						setterMethod.invoke(new_object, (Object) result.getObject((String) fields[i].getName()));
					} else if (fields[i].getGenericType().toString().equals("int")) {
						Method setterMethod = c_2.getMethod(setterName, int.class);
						setterMethod.invoke(new_object, (Object) result.getObject((String) fields[i].getName()));
					}
				}
				list.add(new_object);
			}
			return list;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | SQLException | InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
//	SELECT * 
//	FROM movie_actor AS m_a 
//	JOIN actors AS a ON m_a.actor_id = a.actor_id
//	WHERE m_a.movie_id = (SELECT movie_id FROM movies WHERE movies.title = 'Avengers');

	public void insertJunctionTable(Object object_1, Object object_2) {
		String table_1 = object_1.getClass().getSimpleName();
		String table_2 = object_2.getClass().getSimpleName();
		table_1 = table_1.substring(0, 1).toLowerCase() + table_1.substring(1);
		table_2 = table_2.substring(0, 1).toLowerCase() + table_2.substring(1);
		Class<?> c_1 = object_1.getClass();
		Field[] fields_1 = c_1.getDeclaredFields();
		Class<?> c_2 = object_2.getClass();
		Field[] fields_2 = c_2.getDeclaredFields();
		String getID_1 = fields_1[0].getName();
		String getID_2 = fields_2[0].getName();
		String ID_1 = getID_1.substring(0, 1).toUpperCase() + getID_1.substring(1);
		getID_1 = "get" + ID_1;
		String ID_2 = getID_2.substring(0, 1).toUpperCase() + getID_2.substring(1);
		getID_2 = "get" + ID_2;
		try {
			Method getterID_1 = c_1.getMethod(getID_1);
			Method getterID_2 = c_2.getMethod(getID_2);
			Object ID_1_Value = getterID_1.invoke(object_1);
			Object ID_2_Value = getterID_2.invoke(object_2);
			StringBuilder builder = new StringBuilder("INSERT INTO " + table_1 + "_" + table_2 + "(" + table_1 + "_id, "
					+ table_2 + "_id) VALUES (" + ID_1_Value + ", " + ID_2_Value + ");");
			Connection connection = ConnectionUtil.getConnection(connectionString, connectionUsername,
					connectionPassword, driverName);
			Statement statement = connection.createStatement();
			statement.execute(builder.toString());
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	INSERT INTO movie_actor(movie_id, actor_id) VALUES (
//			(SELECT movie_id FROM movies WHERE movies.title = 'Avengers'),
//			(SELECT actor_id FROM actors WHERE actors.actor_name  = 'Robert Downey Jr.')
//		);

	public void deleteObjectFromDB(int id) {
		StringBuilder builder = new StringBuilder("DELETE FROM " + tableName + " WHERE " + tableName);
		builder.delete(builder.length() - 1, builder.length());
		builder.append("_id = ");
		builder.append(id);
		builder.append(";");
		String sql = builder.toString();
		try (Connection connection = ConnectionUtil.getConnection(connectionString, connectionUsername,
				connectionPassword, driverName)) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(Object object, int id) {
		Class<?> c = object.getClass();
		Field[] fields = c.getDeclaredFields();
		LinkedList<addingObject> list = new LinkedList<addingObject>();
		for (int i = 1; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			try {
				// obtain the getter method from the class we are mapping
				Method getterMethod = c.getMethod(getterName);
				// invoke that method on the object that we are mapping to
				Object fieldValue = getterMethod.invoke(object);
//				System.out.println(field.getName());
//				System.out.println(fieldValue.getClass());
//				System.out.println(fieldValue);
				addingObject new_object = new addingObject(field.getName(), fieldValue.getClass().toString(),
						fieldValue);
				list.add(new_object);
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

		StringBuilder builder = new StringBuilder("UPDATE " + tableName + " SET ");
		for (addingObject x : list) {
			builder.append(x.getFieldName());
			builder.append(" = ?, ");
		}
		builder.delete(builder.length() - 2, builder.length());
		builder.append(" WHERE " + tableName);
		builder.delete(builder.length() - 1, builder.length());
		builder.append("_id = " + id + ";");
//		builder.delete(builder.length() - 2, builder.length());
//		builder.append(");");
		String sql = builder.toString();
		try {
			Connection connection = ConnectionUtil.getConnection(connectionString, connectionUsername,
					connectionPassword, driverName);
			PreparedStatement statement = connection.prepareStatement(sql);

			int count = 0;
			for (addingObject x : list) {
				if (x.getType().equals("class java.lang.String")) {
					statement.setString(++count, (String) x.getValue());
				}
			}
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> getAll(Object object) {
		Class<?> clazz = object.getClass();
		LinkedList<Object> list = new LinkedList<Object>();
		try {
			Constructor<?> constructor = clazz.getConstructor();
//			Object new_object = constructor.newInstance();
			Connection connection = ConnectionUtil.getConnection(connectionString, connectionUsername,
					connectionPassword, driverName);
			StringBuilder builder = new StringBuilder("SELECT * FROM " + tableName + ";");
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(builder.toString());
			while (result.next()) {
				Object new_object = constructor.newInstance();
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					String setterName = "set" + fields[i].getName().substring(0, 1).toUpperCase()
							+ fields[i].getName().substring(1);
					System.out.println(fields[i].getGenericType().toString());
//					System.out.println(fields[i].getClass().getTypeName().toString());
//					System.out.println(setterName);
//					// check type of field as a string
					if (fields[i].getGenericType().toString().equals("class java.lang.String")) {
						Method setterMethod = clazz.getMethod(setterName, String.class);
						setterMethod.invoke(new_object, (Object) result.getObject((String) fields[i].getName()));
					} else if (fields[i].getGenericType().toString().equals("int")) {
						Method setterMethod = clazz.getMethod(setterName, int.class);
						setterMethod.invoke(new_object, (Object) result.getObject((String) fields[i].getName()));
					}
				}
				list.add(new_object);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public Object getByID(Object object, int id) {
		Class<?> clazz = object.getClass();
		try {
			Constructor<?> constructor = clazz.getConstructor();
			Object new_object = constructor.newInstance();
			Connection connection = ConnectionUtil.getConnection(connectionString, connectionUsername,
					connectionPassword, driverName);
			StringBuilder builder = new StringBuilder("SELECT * FROM " + tableName + " WHERE "
					+ tableName.substring(0, tableName.length() - 1) + "_id = " + id + ";");
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(builder.toString());
			if (result.next()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					String setterName = "set" + fields[i].getName().substring(0, 1).toUpperCase()
							+ fields[i].getName().substring(1);
//					System.out.println(fields[i].getClass().getTypeName().toString());
//					System.out.println(setterName);
//					// check type of field as a string
					if (fields[i].getGenericType().toString().equals("class java.lang.String")) {
						Method setterMethod = clazz.getMethod(setterName, String.class);
						setterMethod.invoke(new_object, (Object) result.getObject((String) fields[i].getName()));
					} else if (fields[i].getGenericType().toString().equals("int")) {
						Method setterMethod = clazz.getMethod(setterName, int.class);
						setterMethod.invoke(new_object, (Object) result.getObject((String) fields[i].getName()));
					}
				}
				return new_object;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void persistToDB(Object object) {
		Class<?> c = object.getClass();
		Field[] fields = c.getDeclaredFields();
		LinkedList<addingObject> list = new LinkedList<addingObject>();
		for (int i = 1; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			try {
				// obtain the getter method from the class we are mapping
				Method getterMethod = c.getMethod(getterName);
				// invoke that method on the object that we are mapping to
				Object fieldValue = getterMethod.invoke(object);
//				System.out.println(field.getName());
//				System.out.println(fieldValue.getClass());
//				System.out.println(fieldValue);
				addingObject new_object = new addingObject(field.getName(), fieldValue.getClass().toString(),
						fieldValue);
				list.add(new_object);
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

		StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + "(");
		for (addingObject x : list) {
			builder.append(x.getFieldName());
			builder.append(", ");
		}
		builder.delete(builder.length() - 2, builder.length());
		builder.append(") values (");
		for (addingObject x : list) {
			builder.append("?, ");
			System.out.println(x.getType());
		}
		builder.delete(builder.length() - 2, builder.length());
		builder.append(");");
		String sql = builder.toString();

		try {
			Connection connection = ConnectionUtil.getConnection(connectionString, connectionUsername,
					connectionPassword, driverName);
			PreparedStatement statement = connection.prepareStatement(sql);

			int count = 0;
			for (addingObject x : list) {
				if (x.getType().equals("class java.lang.String")) {
					statement.setString(++count, (String) x.getValue());
				}
			}
			System.out.println(statement.toString());
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Establish ORM connection
		CustomORM actorORM = new CustomORM(
				"jdbc:postgresql://javafs220725.cgbxdd5yszbt.us-east-2.rds.amazonaws.com:5432/movies", "postgres",
				"password", "org.postgresql.Driver", "actors");
		CustomORM movieORM = new CustomORM(
				"jdbc:postgresql://javafs220725.cgbxdd5yszbt.us-east-2.rds.amazonaws.com:5432/movies", "postgres",
				"password", "org.postgresql.Driver", "movies");
		CustomORM actor_movieORM = new CustomORM(
				"jdbc:postgresql://javafs220725.cgbxdd5yszbt.us-east-2.rds.amazonaws.com:5432/movies", "postgres",
				"password", "org.postgresql.Driver", "movie_actor");
		// get my connection object
//
//		Movie movie = new Movie();
//		movie.setTitle("AMERICAN History X");
//		movie.setDescription(
//				"Living a life marked by violence and racism, neo-Nazi Derek Vinyard (Edward Norton) finally goes to prison after killing two black youths who tried to steal his car. Upon his release, Derek vows to change his ways; he hopes to prevent his younger brother, Danny (Edward Furlong), who idolizes Derek, from following in his footsteps. As he struggles with his own deeply ingrained prejudices and watches their mother grow sicker, Derek wonders if his family can overcome a lifetime of hate.");
//		movie.setGenre("Drama");
//		movie.setImage_url(
//				"https://encrypted-tbn3.gstatic.com/images?q=https://flxt.tmsimg.com/assets/p21980_p_v8_ai.jpg");
//		Actor actor = new Actor();
//		actor.setActor_name("");
		// customORM.update(movie, 5);
//		System.out.println(customORM.getAll(movie));
//		System.out.println(customORM.getByID(movie, 1));
//		customORM.persistToDB(movie);
//		customORM.deleteObjectFromDB(4);
//		Actor actor = new Actor();
//		actor = (Actor) actorORM.getByID(actor, 5);
		Movie movie = new Movie();
		movie = (Movie) movieORM.getByID(movie, 1);
		Actor actor = new Actor();
		System.out.println(actorORM.getJoin(movie, actor));
		// actor_movieORM.insertJunctionTable(movie, actor);
	}

}
