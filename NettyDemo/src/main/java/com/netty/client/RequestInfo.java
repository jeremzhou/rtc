package com.netty.client;

import java.util.HashMap;

public class RequestInfo {
	
	private String ip;
	private HashMap<String, Object> CpuPercMap;
	private HashMap<String, Object> MemoryMap;
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public HashMap<String, Object> getCpuPercMap() {
		return CpuPercMap;
	}

	public void setCpuPercMap(HashMap<String, Object> cpuPercMap) {
		CpuPercMap = cpuPercMap;
	}

	public HashMap<String, Object> getMemoryMap() {
		return MemoryMap;
	}

	public void setMemoryMap(HashMap<String, Object> memoryMap) {
		MemoryMap = memoryMap;
	}

	
	
	
}
