package service;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class EditProfileService {
	
	public static Validator selectUser(HttpServletRequest request, int userId) {
		Validator val = new Validator();
		String sessionID = HeaderService.getUserSessionId(request);
		try {
			val = Initializer.rmi.selectUser(userId, sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}

	public static Validator editUser(HttpServletRequest request, UserDto userDto) {
		Validator val = new Validator();
		String sessionID = HeaderService.getUserSessionId(request);	
		try {
			val = Initializer.rmi.updateUser(userDto, sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	public static Validator updatePassword (HttpServletRequest request, UserDto userDto){
		Validator val = new Validator();
		String sessionID = HeaderService.getUserSessionId(request);	
		try {
			val = Initializer.rmi.updateUserPassword(userDto, sessionID);
			/*if (val.isVerified()){
				HeaderService.logout();
			}*/
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	public static Validator resetPassword(String email){
		Validator val=new Validator();
		try{
			val=Initializer.rmi.resetPassword(email);
		}
		catch(RemoteException e){
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
}
