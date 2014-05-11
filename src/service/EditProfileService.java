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
			val = Initializer.getRmi().selectUser(userId, sessionID);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}

	public static Validator editUser(HttpServletRequest request, UserDto userDto) {
		Validator val = new Validator();
		String sessionID = HeaderService.getUserSessionId(request);	
		try {
			val = Initializer.getRmi().updateUser(userDto, sessionID);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}
	
	public static Validator updatePassword (HttpServletRequest request, UserDto userDto){
		Validator val = new Validator();
		String sessionID = HeaderService.getUserSessionId(request);	
		try {
			val = Initializer.getRmi().updateUserPassword(userDto, sessionID);
			/*if (val.isVerified()){
				HeaderService.logout();
			}*/
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}
	
	public static Validator resetPassword(String email){
		Validator val=new Validator();
		try{
			val=Initializer.getRmi().resetPassword(email);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			
		}
		return val;
	}
	
}
