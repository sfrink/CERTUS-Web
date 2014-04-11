package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.UserDto;
import dto.Validator;
import enumeration.UserStatus;

/**
 * 
 * @author Hirosh Wickramasuriya
 *
 */
/**
 * @author sulo
 *
 */
public class UserService
{
	/**
	 * @param userDto - user object
	 * @return Validator with the userDto including the primary key assigned by the db.
	 */
		
	/**
	 * @param userDto - user object
	 * @return Validator with the verified status true upon successful update, false otherwise.
	 */
	public static Validator editUser(UserDto userDto) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId();
			val = Initializer.rmi.editUser(userDto, sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	/**
	 * @param userId - user identification number (pk) 
	 * @return Validator with UserDto containing user information for the given user id 
	 */ 
	public static Validator selectUser(int userId) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId();
			val = Initializer.rmi.selectUser(userId, sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	/**
	 * @return - Validator with ArrayList<UserDto> all the users in the system
	 */
	public static Validator selectAllUsers() {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId();
			val = Initializer.rmi.selectAllUsers(sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	/**
	 * @param userId - user identification number 
	 * @return Validator with status true upon successful update of user status, false otherwise
	 */
	public static Validator activateUser(int userId) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId();
			val = Initializer.rmi.editUserStatus(userId, UserStatus.ACTIVE, sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	/**
	 * @param userId - user identification number 
	 * @return Validator with status true upon successful update of user status, false otherwise
	 */
	public static Validator lockUser(int userId) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId();
			val = Initializer.rmi.editUserStatus(userId, UserStatus.LOCKED, sessionID);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
}
