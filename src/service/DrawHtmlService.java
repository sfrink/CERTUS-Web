package service;

public class DrawHtmlService {

	
	/**
	 * This function returns HTML code for alert
	 * @param message
	 * @param mode
	 * @return
	 */
	public static String drawMessageAlert(String message, String mode) {
		String out = "";

		if(message != null && !message.equals("")) {
			out += "<div data-alert class=\"alert-box radius " + mode + "\">";
			out += "<b>" + message + "</b>";
			out += "<a href=\"\" class=\"close\">x</a>";
			out += "</div>";
		}
		
		return out;
	}
	
	/**
	 * This function returns HTML code for label
	 * @param message
	 * @param mode
	 * @return
	 */
	public static String drawMessageLabel(String message, String mode) {
		String out = "";

		if(message != null && !message.equals("")) {
		  out += "<span class=\"label " + mode + "\">" + message + "</span>";
		}
		
		return out;
	}

	
	
}
