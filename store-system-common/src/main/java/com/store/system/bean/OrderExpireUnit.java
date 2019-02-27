package com.store.system.bean;

import com.google.common.collect.Maps;

import java.util.Map;


public enum OrderExpireUnit {

    nvl(0, "null"),
	inday(1, "1c"),
	minutes(2, "m"),
	hours(3, "h"),
	day(4, "d");
	
	
	public static OrderExpireUnit get(int id) {
		OrderExpireUnit[] arr = OrderExpireUnit.values();
		Map<Integer, OrderExpireUnit> map = Maps.newHashMap();
		for(OrderExpireUnit one : arr) {
			map.put(one.getId(), one);
		}
		return map.get(id);
	}
	
	private int id;
	
	private String sign;
	
    OrderExpireUnit(int id, String sign) {
		this.id = id;
		this.sign = sign;
	}

	public int getId() {
		return id;
	}

	public String getSign() {
		return sign;
	}
    
	
}
