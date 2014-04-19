package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class LoginService {
	
	public static Validator authenticate(String username, String password) {
		Validator v = new Validator();
		v.setStatus("Authentication Failed");
		v.setVerified(false);
	
		try {
			// check if RMI is initially down
			if(Initializer.rmi != null) {
				v = Initializer.rmi.checkIfUsernamePasswordMatch(username, password);
			} else {
				v.setVerified(false);
				v.setStatus("Error. The server is down. Please try to reconnect later.");			
				Initializer i = new Initializer();
				i.dummy();
			}
		} catch (RemoteException e) {
			v.setVerified(false);
			v.setStatus("The application has encountered problem establishing RMI connection");
			
			Initializer i = new Initializer();
			i.dummy();
		}
		
		return v;
	}
	
	public static Validator authenticateTemp(String username, String password, String newPassword){
		Validator v = new Validator();
		v.setStatus("Authentication Failed");
		v.setVerified(false);
	
		try {
			// check if RMI is initially down
			if(Initializer.rmi != null) {
				v = Initializer.rmi.checkIfUsernameTempPasswordMatch(username, password, newPassword);
			} else {
				v.setVerified(false);
				v.setStatus("Error. The server is down. Please try to reconnect later.");			
				Initializer i = new Initializer();
				i.dummy();
			}
		} catch (RemoteException e) {
			v.setVerified(false);
			v.setStatus("The application has encountered problem establishing RMI connection");
			
			Initializer i = new Initializer();
			i.dummy();
		}
		
		return v;
	}
	
}
