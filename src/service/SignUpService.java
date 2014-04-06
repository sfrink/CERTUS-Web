package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class SignUpService {

	public static Validator addUser(UserDto newUser){
		Validator res = new Validator();
		
		try {
			res = Initializer.rmi.registerNewUser(newUser);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		
		return res;
	}
	
}
