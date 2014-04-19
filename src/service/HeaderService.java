package service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import rmi.Initializer;

public class HeaderService {

	// dummy authentication mechanism
	public static boolean auth = false;
	public static int userId;
	public static String userSessionId;
	public static String userName;
	public static int userType;
	public static boolean tempUser = false;
	public static String userEmail = "";
	public static boolean loginWithTemp=false;
	

	public static String getUserEmail() {
		return userEmail;
	}

	public static void setUserEmail(String userEmail) {
		HeaderService.userEmail = userEmail;
	}

	public static boolean isTempUser() {
		return tempUser;
	}

	public static void setTempUser(boolean tempUser) {
		HeaderService.tempUser = tempUser;
	}

	public static boolean isAuthenticated() {
		return auth;
	}

	public static void authenticate() {
		auth = true;
	}
	
	public static void deAuthenticate() {
		auth = false;
	}
	
	public static void setLoginWithTemp(boolean b){
		loginWithTemp=b;
	}
	
	
	
	public static boolean usedTemp(){
		return loginWithTemp;
	}

	public static int getUserId() {
		return userId;
	}

	public static void setUserId(int userId) {
		HeaderService.userId = userId;
	}

	public static String getUserSessionId() {
		return userSessionId;
	}

	public static void setUserSessionId(String userSessionId) {
		HeaderService.userSessionId = userSessionId;
	}
	
	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		HeaderService.userName = userName;
	}
	
	public static int getUserType() {
		return userType;
	}

	public static void setUserType(int userType) {
		HeaderService.userType = userType;
	}

	public static void logout() {
		try {
			Initializer.rmi.logOut(getUserSessionId());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deAuthenticate();
		setUserId(0);
		setUserSessionId("");
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function checks whether this user has access to servlet
	 * NOTE: this local platform restriction
	 * @param servletName
	 * @return
	 */
	public static boolean hasAccess(String servletName) {
		String[] adminServlets = {"adminElection", "adminUser"};
		String[] userServlets = {"election", "voting", "results", "newkey", "profile", "upload"};

		if(getUserType() == 1) {
			// ADMIN
			return Arrays.asList(adminServlets).contains(servletName); 
		} else if(getUserType() == 0) {
			// USER
			return Arrays.asList(userServlets).contains(servletName); 
		}

		return false;
	}
}	
