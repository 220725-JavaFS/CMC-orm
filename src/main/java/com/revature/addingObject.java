package com.revature;

public class addingObject {

	private String type;
	private String fieldName;
	private Object value;

	public addingObject(String fieldName, String type, Object value) {
		super();
		this.type = type;
		this.value = value;
		this.setFieldName(fieldName);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
