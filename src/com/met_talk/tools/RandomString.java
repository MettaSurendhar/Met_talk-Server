package com.met_talk.tools;

public class RandomString {
	
	private RandomString() {}
	private static RandomString instance;
	
	public static RandomString getInstance() {
		if (instance == null) {
			instance = new RandomString();
		}
		return instance;
	}
	
	public String getRandomString(int n)
	 {
	  String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	         + "0123456789"
	         + "abcdefghijklmnopqrstuvxyz";
	 
	  StringBuilder strBuilRandom = new StringBuilder(n);
	  for (int i = 0; i < n; i++) {
	   int iRandom = (int)(AlphaNumericString.length()* Math.random());
	   strBuilRandom.append(AlphaNumericString
	      .charAt(iRandom));
	  }
	  
	  String strRandom = strBuilRandom.toString();
	  return strRandom;
	 }




}
