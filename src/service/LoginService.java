package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.Validator;

public class LoginService {
	
	public static Validator authenticate(String username, String password) {
		Validator v = new Validator();
		v.setStatus("Authentication Failed");
		v.setVerified(false);
		
		System.out.println(username + " " +password);
		
		try {
			v = Initializer.rmi.checkIfUsernamePasswordMatch(username, password);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return v;
	}
	
	
	
}
