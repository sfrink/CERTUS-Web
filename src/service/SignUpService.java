package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class SignUpService {

	public static Validator addBasicUser(UserDto newUser){
		Validator res = new Validator();
		
		try {
			res = Initializer.rmi.addUser(newUser);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	}
	
	
	public static Validator addUserwithKeyProtectionPassword(UserDto newUser){
		Validator res = new Validator();
		
		try {
			res = Initializer.rmi.addUserWithPP(newUser);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	}
	
	
	public static Validator addUserWithPublicKey(UserDto newUser){
Validator res = new Validator();
		
		try {
			res = Initializer.rmi.addUserWithKey(newUser);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	}
	
}
