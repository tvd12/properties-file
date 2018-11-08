package com.monkey.properties.file.testing;

import java.util.ArrayList;
import java.util.List;

public class Lists {
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> newArrayList(T...ts) {
		List<T> list = new ArrayList<>();
		for(T t : ts)
			list.add(t);
		return list;
	}
	
}
