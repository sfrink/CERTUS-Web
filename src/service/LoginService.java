package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class LoginService {
	private static boolean repeatRmi = true;

	public static Validator authenticate(String username, String password) { 
		Validator v = new Validator();
		v.setStatus("Authentication Failed");
		v.setVerified(false);
	
		try {
			v = Initializer.getRmi().checkIfUsernamePasswordMatch(username, password);
			repeatRmi = true;
		} catch (RemoteException e) {
			if(repeatRmi) {
				// Revoke RMI one more time
				Initializer.connectRmiServer();
				repeatRmi = false;
				v = authenticate(username, password);
			} else {
				// Give out error message
				v.setVerified(false);
				v.setStatus("ERROR: the application has encountered problem establishing RMI connection");
				repeatRmi = true;
			}
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
				Initializer.connectRmiServer();
			}
		} catch (RemoteException e) {
			v.setVerified(false);
			v.setStatus("The application has encountered problem establishing RMI connection");
			
			Initializer.connectRmiServer();
		}
		
		return v;
	}
	
}
