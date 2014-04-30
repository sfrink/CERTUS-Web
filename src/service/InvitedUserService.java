package service;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class InvitedUserService {
	
	
	public static Validator addUpdateUser(HttpServletRequest request, UserDto newUser){
		Validator res = new Validator();
		
		String sessionID = HeaderService.getUserSessionId(request);
		
		try {
			res = Initializer.rmi.updateTempUser(newUser, sessionID);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	}
	
	
	public static Validator updateUserwithKeyProtectionPassword(HttpServletRequest request, UserDto newUser){
		Validator res = new Validator();

		String sessionID = HeaderService.getUserSessionId(request);
		
		try {
			res = Initializer.rmi.UpdateTempUserWithPP(newUser, sessionID);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	} 
	
	

	public static Validator updateUserWithPublicKey(HttpServletRequest request, UserDto newUser){
		Validator res = new Validator();
		
		String sessionID = HeaderService.getUserSessionId(request);
		
		try {
			res = Initializer.rmi.UpdateTempUserWithKey(newUser, sessionID);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	} 

}
