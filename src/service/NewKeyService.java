package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.Validator;

public class NewKeyService {

	
	public static Validator generateNewKeys(int userID, String newKeyPass, String sessionID){
		Validator res = new Validator();
		
		try {
			res = Initializer.rmi.generateNewKeys(userID, newKeyPass, sessionID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		return res;
	}
	
	
}
