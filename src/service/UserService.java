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
public class UserService
{
	/**
	 * @param userDto
	 * @return Validator with the userDto including the primary key assigned by the db.
	 */
	public static Validator addUser(UserDto userDto) {
		Validator val = new Validator();

		try {
			val = Initializer.rmi.addUser(userDto);
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
			val = Initializer.rmi.selectAllUsers();
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	/**
	 * @param userDto
	 * @return Validator with status true upon successful update of user status, false otherwise
	 */
	public static Validator activateUser(int userId) {
		Validator val = new Validator();

		try {
			val = Initializer.rmi.editUserStatus(userId, UserStatus.ACTIVE);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	/**
	 * @param userDto
	 * @return Validator with status true upon successful update of user status, false otherwise
	 */
	public static Validator lockUser(int userId) {
		Validator val = new Validator();

		try {
			val = Initializer.rmi.editUserStatus(userId, UserStatus.LOCKED);
		} catch (RemoteException e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
}
