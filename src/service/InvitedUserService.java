package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class InvitedUserService {
	
	
	public static Validator addUpdateUser(UserDto newUser, String tempPassword){
		Validator res = new Validator();
		
		String sessionID = HeaderService.getUserSessionId();
		
		try {
			res = Initializer.rmi.updateTempUser(newUser, tempPassword, sessionID);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	}
	
	
	public static Validator updateUserwithKeyProtectionPassword(UserDto newUser, String tempPassword){
		Validator res = new Validator();
		
		String sessionID = HeaderService.getUserSessionId();
		
		try {
			res = Initializer.rmi.UpdateTempUserWithPP(newUser, tempPassword, sessionID);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	} 
	
	

	public static Validator updateUserWithPublicKey(UserDto newUser, String tempPassword){
		Validator res = new Validator();
		
		String sessionID = HeaderService.getUserSessionId();
		
		try {
			res = Initializer.rmi.UpdateTempUserWithKey(newUser, tempPassword, sessionID);
		} catch (RemoteException e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		
		return res;
	} 

	public static Validator resendInvitation(UserDto u){
		Validator val=new Validator();
		String sessionID = HeaderService.getUserSessionId();
		try{
			Initializer.rmi.resendInvitation(u,sessionID);
		}
		catch(RemoteException e){
			val.setVerified(false);
			val.setStatus("RMI failed");
		}
		
		
		return val;
	}

}
