package com.taskmanager.dto;

import java.util.List;

import com.taskmanager.model.MyTask;

//@JsonPropertyOrder({ "content", "last", "pageNo", "pageSize", "totalElements", "totalPages" })
public class TaskResponse {

	private List<MyTask> content;
	private boolean last;
	private int pageNo;
	private int pageSize;
	private Long totalElements;
	private int totalPages;

	public TaskResponse() {
		this.content = null;
		this.last = false;
		this.pageNo = 0;
		this.pageSize = 0;
		this.totalElements = 0L;
		this.totalPages = 0;

	}

	public TaskResponse(
			List<MyTask> content,
			int pageNo,
			int pageSize,
			Long totalElements,
			int totalPages,
			boolean last) {
		super();
		this.content = content;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.last = last;
	}

	public List<MyTask> getConent() {
		return content;
	}

	public boolean getLast() {
		return last;
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

	public void setContent(List<MyTask> content) {
		this.content = content;
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

}
