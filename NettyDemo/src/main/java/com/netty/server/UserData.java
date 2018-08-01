package com.netty.server;

public class UserData {
	
	private String id;
	private String name;
	private String responseMessage;
	
	public UserData() {}
	public UserData(String id, String name, String responseMessage) {
		super();
		this.id = id;
		this.name = name;
		this.responseMessage = responseMessage;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "UserData [id=" + id + ", name=" + name + ", responseMessage=" + responseMessage + "]";
	}
	
	
}
