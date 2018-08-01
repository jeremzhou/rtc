package com.netty.client;

public class UserParam {
	private String id;
	private String name;
	private String requestMessage;
	
	public UserParam() {}
	public UserParam(String id, String name, String requestMessage) {
		super();
		this.id = id;
		this.name = name;
		this.requestMessage = requestMessage;
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
	public String getRequestMessage() {
		return requestMessage;
	}
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	@Override
	public String toString() {
		return "UserParam [id=" + id + ", name=" + name + ", requestMessage=" + requestMessage + "]";
	}
	
	
}
