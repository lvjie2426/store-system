package com.store.system.client;

import com.google.common.collect.Lists;
import com.s7.baseFramework.model.pagination.Pager;

import java.util.List;

public class PagerResult<E> {


	/**
	 * 当前页数
	 */
	private int pageNumber;
	/**
	 * 每页显示条数
	 */
	private int pageSize;
	/**
	 * 数据
	 */
	private List<E> rows;


	private int startRecord;

	private double num;

	/**
	 * 总页数
	 */
	private int total;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getStartRecord() {
		return startRecord;
	}

	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public double getNum() {
		return num;
	}

	public void setNum(double num) {
		this.num = num;
	}

	public Pager getPager(){

//		int size;
//		int i = this.recordCount / this.pageSize;
//		int j = this.recordCount % this.pageSize;
//		if(j == 0){
//			size = i;
//		}else {
//			size = i+1;
//		}
//		if(this.pageCount>0){
//			if(this.nowPage>size){
//				this.nowPage = size;
//			}
//		}


		Pager pager = new Pager();
		pager.setSize(this.pageSize);
		pager.setPage(this.pageNumber);
		return pager;
	}


	public PagerResult() {
	}

	public PagerResult(Pager pager) {

		long pageCount;
		long pCount = pager.getTotalCount() / pager.getSize();

		if(pager.getTotalCount() % pager.getSize() == 0){
			pageCount = pCount;
		}else {
			pageCount = pCount +1;
		}
		this.total = (int)pager.getTotalCount();
		this.rows = pager.getData();
		if(null == this.rows) {
		    this.rows = Lists.newArrayList();
        }
	}


}
