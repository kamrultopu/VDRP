package com.vdrp.client.model;

public class User {
	String username;
	String password;
	public User(String user, String pass){
		username = user;
		password = pass;
	}
	public boolean IsPassWordOk(String pass){
		if( password.compareTo(pass) == 0 ){
			return true;
		}
		return false;
	}
}
