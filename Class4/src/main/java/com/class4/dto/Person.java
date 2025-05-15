package com.class4.dto;

public class Person {

	private String name;
	private int age;
	private IDType docType;
	private String docId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public IDType getDocType() {
		return docType;
	}

	public void setDocType(IDType docType) {
		this.docType = docType;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

}
