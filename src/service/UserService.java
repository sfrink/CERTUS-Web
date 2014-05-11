package service;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

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
	public static Validator editUser(HttpServletRequest request, UserDto userDto) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId(request);
			val = Initializer.getRmi().editUser(userDto, sessionID);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}
	
	/**
	 * @param userId - user identification number (pk) 
	 * @return Validator with UserDto containing user information for the given user id 
	 */ 
	public static Validator selectUser(HttpServletRequest request, int userId) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId(request);
			val = Initializer.getRmi().selectUser(userId, sessionID);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}
	
	/**
	 * @return - Validator with ArrayList<UserDto> all the users in the system
	 */
	public static Validator selectAllUsers(HttpServletRequest request) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId(request);
			val = Initializer.getRmi().selectAllUsers(sessionID);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}
	
	/**
	 * @param userId - user identification number 
	 * @return Validator with status true upon successful update of user status, false otherwise
	 */
	public static Validator activateUser(HttpServletRequest request, int userId) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId(request);
			val = Initializer.getRmi().editUserStatus(userId, UserStatus.ACTIVE, sessionID);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}
	
	/**
	 * @param userId - user identification number 
	 * @return Validator with status true upon successful update of user status, false otherwise
	 */
	public static Validator lockUser(HttpServletRequest request, int userId) {
		Validator val = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId(request);
			val = Initializer.getRmi().editUserStatus(userId, UserStatus.LOCKED, sessionID);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}
}
