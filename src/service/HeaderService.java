package service;

import java.rmi.RemoteException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import rmi.Initializer;

public class HeaderService {

	public static void resetSession(HttpServletRequest request) {
		request.getSession().setAttribute("auth", false);
		request.getSession().setAttribute("userId", -1);
		request.getSession().setAttribute("userSessionId", "");
		request.getSession().setAttribute("userName", "");
		request.getSession().setAttribute("userType", -1);
		request.getSession().setAttribute("tempUser", false);
		request.getSession().setAttribute("userEmail", "");
		request.getSession().setAttribute("loginWithTemp", false);
	}
	
	public static String sessionToString(HttpServletRequest request) {
		String out = "";
		out += request.getSession().getAttribute("auth").toString();
		out += request.getSession().getAttribute("userId").toString();
		out += request.getSession().getAttribute("userSessionId").toString();
		out += request.getSession().getAttribute("userName").toString();
		out += request.getSession().getAttribute("userType").toString();
		out += request.getSession().getAttribute("tempUser").toString();
		out += request.getSession().getAttribute("userEmail").toString();
		out += request.getSession().getAttribute("loginWithTemp").toString();
		return out;
	}
	
	public static boolean isSessionStarted(HttpServletRequest request) {
		boolean out = false;

		if(request.getSession().getAttribute("sessionStart") != null) {
			out = (boolean) request.getSession().getAttribute("sessionStart");
		}
		
		return out;		
	}

	public static void startSession(HttpServletRequest request) {
		request.getSession().setAttribute("sessionStart", true);
	}

	public static void stopSession(HttpServletRequest request) {
		request.getSession().setAttribute("sessionStart", false);
	}	

	public static String getUserEmail(HttpServletRequest request) {
		String value = (String) request.getSession().getAttribute("userEmail");
		return value;
	}

	public static void setUserEmail(HttpServletRequest request, String userEmail) {
		request.getSession().setAttribute("userEmail", userEmail);
	}
	
	public static boolean isTempUser(HttpServletRequest request) {
		boolean value = (boolean) request.getSession().getAttribute("tempUser");
		return value;
	}

	public static void setTempUser(HttpServletRequest request, boolean tempUser) {
		request.getSession().setAttribute("tempUser", tempUser);
	}
	
	public static boolean isAuthenticated(HttpServletRequest request) {
		boolean value = (boolean) request.getSession().getAttribute("auth");
		return value;
	}

	public static void authenticate(HttpServletRequest request) {
		request.getSession().setAttribute("auth", true);
	}
	
	public static void deAuthenticate(HttpServletRequest request) {
		request.getSession().setAttribute("auth", false);
	}
	
	public static void setLoginWithTemp(HttpServletRequest request, boolean b){
		request.getSession().setAttribute("loginWithTemp", b);
	}
	
	
	
	public static boolean usedTemp(HttpServletRequest request){
		boolean value = (boolean) request.getSession().getAttribute("loginWithTemp");
		return value;
	}

	public static int getUserId(HttpServletRequest request) {
		int value = (int) request.getSession().getAttribute("userId");
		return value;
	}

	public static void setUserId(HttpServletRequest request, int userId) {
		request.getSession().setAttribute("userId", userId);
	}

	public static String getUserSessionId(HttpServletRequest request) {
		String value = (String) request.getSession().getAttribute("userSessionId");
		return value;
	}

	public static void setUserSessionId(HttpServletRequest request, String userSessionId) {
		request.getSession().setAttribute("userSessionId", userSessionId);
	}
	
	public static String getUserName(HttpServletRequest request) {
		String value = (String) request.getSession().getAttribute("userName");
		return value;
	}

	public static void setUserName(HttpServletRequest request, String userName) {
		request.getSession().setAttribute("userName", userName);
	}
	
	public static int getUserType(HttpServletRequest request) {
		int value = (int) request.getSession().getAttribute("userType");
		return value;		
	}

	public static void setUserType(HttpServletRequest request, int userType) {
		request.getSession().setAttribute("userType", userType);
	}

	public static void logout(HttpServletRequest request) {
		try {
			Initializer.rmi.logOut(getUserSessionId(request));
			request.getSession().invalidate();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// reset all session variables
		resetSession(request);
	}
	
	
	/**
	 * Dmitriy Karmazin
	 * This function checks whether this user has access to servlet
	 * NOTE: this local platform restriction
	 * @param servletName
	 * @return
	 */
	public static boolean hasAccess(HttpServletRequest request, String servletName) {
		String[] adminServlets = {"adminElection", "adminUser"};
		String[] userServlets = {"election", "newkey", "profile", "upload", "uploadkeypage"};

		if(getUserType(request) == 1) {
			// ADMIN
			return Arrays.asList(adminServlets).contains(servletName); 
		} else if(getUserType(request) == 0) {
			// USER
			return Arrays.asList(userServlets).contains(servletName); 
		}

		return false;
	}
}	
