package com.taskmanager.dto;

import java.util.List;

public class MyUserResponse {

	private List<MyUserDto> username;
	private boolean last;
	private int pageNo;
	private int pageSize;
	private Long totalElements;
	private int totalPages;

	public MyUserResponse() {

	}

	public MyUserResponse(
			List<MyUserDto> username,
			boolean last,
			int pageNo,
			int pageSize,
			Long totalElements,
			int totalPages) {
		super();
		this.username = username;
		this.last = last;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public List<MyUserDto> getUsername() {
		return username;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setUsername(List<MyUserDto> username) {
		this.username = username;
	}

}
