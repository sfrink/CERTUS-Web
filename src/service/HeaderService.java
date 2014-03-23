package service;
public class HeaderService {

	// dummy authentication mechanism
	public static boolean auth = false;
	public static int userId;
	
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
	
	
	
}
