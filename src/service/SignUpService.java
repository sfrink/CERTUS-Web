package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class SignUpService {
	private static boolean repeatRmi = true;

	public static Validator addBasicUser(UserDto newUser){
		Validator v = new Validator();
		
		try {
			v = Initializer.getRmi().addUser(newUser);
			repeatRmi = true;
		} catch (Exception e) {
			if(repeatRmi) {
				// Revoke RMI one more time
				Initializer.connectRmiServer();
				repeatRmi = false;
				v = addBasicUser(newUser);
			} else {
				// Give out error message
				v.setVerified(false);
				v.setStatus("ERROR: the application has encountered problem establishing RMI connection");
				repeatRmi = true;
			}
		}
		
		return v;
	}
	
	
	public static Validator addUserwithKeyProtectionPassword(UserDto newUser){
		Validator v = new Validator();
		
		try {
			v = Initializer.getRmi().addUserWithPP(newUser);
			repeatRmi = true;
		} catch (Exception e) {
			if(repeatRmi) {
				// Revoke RMI one more time
				Initializer.connectRmiServer();
				repeatRmi = false;
				v = addUserwithKeyProtectionPassword(newUser);
			} else {
				// Give out error message
				v.setVerified(false);
				v.setStatus("ERROR: the application has encountered problem establishing RMI connection");
				repeatRmi = true;
			}
		}
		
		return v;
	}
	
	
	public static Validator addUserWithPublicKey(UserDto newUser){
		Validator v = new Validator();
		
		try {
			v = Initializer.getRmi().addUserWithKey(newUser);
			repeatRmi = true;
		} catch (Exception e) {
			if(repeatRmi) {
				// Revoke RMI one more time
				Initializer.connectRmiServer();
				repeatRmi = false;
				v = addUserWithPublicKey(newUser);
			} else {
				// Give out error message
				v.setVerified(false);
				v.setStatus("ERROR: the application has encountered problem establishing RMI connection");
				repeatRmi = true;
			}
		}

		return v;
	}
}
