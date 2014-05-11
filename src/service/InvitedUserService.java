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
			res = Initializer.getRmi().updateTempUser(newUser, sessionID);
		} catch (Exception e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
			HeaderService.errorLogout(request);
		}
		
		return res;
	}
	
	
	public static Validator updateUserwithKeyProtectionPassword(HttpServletRequest request, UserDto newUser){
		Validator res = new Validator();

		String sessionID = HeaderService.getUserSessionId(request);
		
		try {
			res = Initializer.getRmi().UpdateTempUserWithPP(newUser, sessionID);
		} catch (Exception e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
			HeaderService.errorLogout(request);
		}
		
		return res;
	} 
	
	

	public static Validator updateUserWithPublicKey(HttpServletRequest request, UserDto newUser){
		Validator res = new Validator();
		
		String sessionID = HeaderService.getUserSessionId(request);
		
		try {
			res = Initializer.getRmi().UpdateTempUserWithKey(newUser, sessionID);
		} catch (Exception e) {
			res.setVerified(false);
			res.setStatus("RMI failure.");
			HeaderService.errorLogout(request);
		}
		
		return res;
	} 

}
