package ru.prime.app.validator;

import java.text.ParseException;

public class Constraint {

	private ConstraintType type=ConstraintType.NONE;
	private String value="";
	private String regexp="";
	private String inclusive="";
	private String min="";
	private String max="";
	private String integer="";
	private String fraction="";
	private String flag="";
	private String message="";
	
	public ConstraintType getType() {
		return type;
	}
	public void setType(ConstraintType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRegexp() {
		return regexp;
	}
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	public String getInclusive() {
		return inclusive;
	}
	public void setInclusive(String inclusive) {
		this.inclusive = inclusive;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getInteger() {
		return integer;
	}
	public void setInteger(String integer) {
		this.integer = integer;
	}
	public String getFraction() {
		return fraction;
	}
	public void setFraction(String fraction) {
		this.fraction = fraction;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}		
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
	//Validate functions

	public boolean checkUniqueness(Object value){
		return false;
	}
	
	
	public boolean validateValue(long value){
		boolean result = false;
		switch(this.getType()){
		case DECIMAL_MAX:
			
			break;
		case DECIMAL_MIN:
			break;
		case DIGITS:
			break;
		case MAX:
			break;
		case MIN:
			break;
		case NOT_NULL:
			if(value != 0)return true;
			break;
		case UNIQUE:
			return checkUniqueness(value);
		default:
		
		
		}
		return result;
	}
	
	public boolean validateValue(boolean value){
		return false;
	}
	/*
	 * What is about date format?
	 * 
	 */
	public boolean validateValue(java.sql.Date value){
		boolean result = false;
		java.util.Date current = new java.util.Date();
		switch(this.getType()){
		case FUTURE:
			if(current.before(value))result = true;
			break;
		case PAST:
			if(current.after(value))result = true;
			break;
		case MAX:
			//Here I have to parse data in some way
			break;
		case MIN:
			break;
		case NOT_NULL:
			if(value != null) result = true;
			break;
		case UNIQUE:
			return checkUniqueness(value);
		default:
			return false;
		
		}
		return false;		
	}
	
	public boolean validateValue(java.util.Date value){
		
		return false;
	}
	private static boolean stringToBool(String value){
		boolean result = false;
		if(value == null)return false;
		if(value.trim().toLowerCase().equals("true"))result = true; 
		if(value.trim().toLowerCase().equals("y"))result = true; 
		if(value.trim().toLowerCase().equals("on"))result = true; 
		if(value.trim().toLowerCase().equals("yes"))result = true;
		return result;
	}
	
	//TODO I have to validated correctly date, string, boolean
	public boolean validateValue(String value){
		boolean result = false;
		switch(this.getType()){
		case ASSERT_TRUE:
			if(value == null)break;
			if(value.trim().toLowerCase().equals("true"))result = true; 
			if(value.trim().toLowerCase().equals("y"))result = true; 
			if(value.trim().toLowerCase().equals("on"))result = true; 
			if(value.trim().toLowerCase().equals("yes"))result = true; 
			break;
		case ASSERT_FALSE:
			if(value == null)break;
			if(value.trim().toLowerCase().equals("false"))result = true; 
			if(value.trim().toLowerCase().equals("n"))result = true; 
			if(value.trim().toLowerCase().equals("off"))result = true; 
			if(value.trim().toLowerCase().equals("no"))result = true; 
			break;
		case DECIMAL_MAX:
			try{
				long max = Long.parseLong(this.getValue());
				long val = Long.parseLong(value);
				if(val < max)result = true;
			}catch(NumberFormatException ex){
				//It wasn't integers
				try{
					double dmax = Double.parseDouble(this.getMax());
					double dval = Double.parseDouble(value);
					if(dval < dmax)result = true;
				}catch(NumberFormatException ex2){
					result = false;
				}
			}			
			break;
		case DECIMAL_MIN:
			try{
				long max = Long.parseLong(this.getMax());
				long min = Long.parseLong(this.getMin());
				long val = Long.parseLong(value);
				if((val > min) && (val < max))result = true;
			}catch(NumberFormatException ex){
				//It wasn't integers
				try{
					double dmax = Double.parseDouble(this.getMax());
					double dmin = Double.parseDouble(this.getMin());
					double dval = Double.parseDouble(value);
					if((dval > dmin) && (dval < dmax))result = true;
				}catch(NumberFormatException ex2){
					result = false;
				}
			}
			break;
		case DIGITS:
			break;
		case FUTURE:
			//TODO it is better process in date processor
			break;
		case PAST:
			//TODO it is better process in date processor				
			break;
		case MAX:
			break;
		case MIN:
			break;
		case NOT_NULL:
			break;
		case PATTERN:
			java.util.regex.Pattern pt = java.util.regex.Pattern.compile(this.getRegexp());
			java.util.regex.Matcher mt = pt.matcher(value);
			result = mt.matches();
			break;
		case SIZE:

			
			break;
		case UNIQUE:
			return checkUniqueness(value);
		default:
		
		
		}
		return result;
	}
	
}