package ru.prime.app.validator;

import java.util.ArrayList;
import java.util.List;

public class Field{
	private String name;
	private List<Constraint> constraintList = new ArrayList<Constraint>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addConstraint(Constraint constraint){
		constraintList.add(constraint);
	}
	
}