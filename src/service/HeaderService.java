package service;
public class HeaderService {

	// dummy authentication mechanism
	public static boolean auth = false;
	public static int userId;
	public static String userSessionId;
	public static String userName;

	public static boolean isAuthenticated() {
		return auth;
	}

	public static void authenticate() {
		auth = true;
	}
	
	public static void deAuthenticate() {
		auth = false;
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


	
	public static void logout() {
		deAuthenticate();
		setUserId(0);
		setUserSessionId("");
	}
	
	
}
