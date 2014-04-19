package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;

public class EditProfileService {
	
	public static Validator selectUser(int userId) {
		Validator val = new Validator();
		String sessionID = HeaderService.getUserSessionId();
		try {
			val = Initializer.rmi.selectUser(userId, sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}

	public static Validator editUser(UserDto userDto) {
		Validator val = new Validator();
		String sessionID = HeaderService.getUserSessionId();	
		try {
			val = Initializer.rmi.updateUser(userDto, sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	public static Validator updatePassword (UserDto userDto){
		Validator val = new Validator();
		String sessionID = HeaderService.getUserSessionId();	
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
	
	
	public static Validator sendTempPassword(UserDto userDto){
		Validator val=new Validator();
		String sessionID = HeaderService.getUserSessionId();	
		try {
			val = Initializer.rmi.sendTempPassword(userDto, sessionID);
			
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
}
