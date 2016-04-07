package ru.prime.app.validator;

import java.util.ArrayList;
import java.util.List;

public class DataEntity{
	private String name;
	private List<Field> fieldList = new ArrayList<Field>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addField(Field field){
		fieldList.add(field);
	}
	
}