package service;

import dto.Validator;

public class LoginService {

	private static String def_username = "jgalt@jj.com";
	private static String def_password = "cdsnJewqq1";
	
	public static Validator authenticate(String username, String password) {
		Validator v = new Validator();
		v.setStatus("Authentication Failed");
		v.setVerified(false);
		
		if(username == null || password == null ||
		   username.isEmpty() || password.isEmpty()) {
			return v;
		}
		
		if(username.equals(def_username) && password.equals(def_password)) {
			v.setStatus("Authenticated");
			v.setVerified(true);
			return v;
		}
	
		return v;
	}
	
	
	
}
