package service;

public class HtmlService {

	
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

	
	
	
	public static String drawInputTextAlphanumeric(String field_name, String label, String placeholder, String value) {
		String out = "";
		
		out += "<div class=\"" + field_name + "\">";
		out += "<label>" + label + " <small>required</small>";
		out += "<input type=\"text\" name=\""+ field_name + "\" placeholder=\"" + placeholder + "\" value=\"" + value + "\" required pattern=\"^[0-9a-zA-Z\\s\\r\\n]+$\"\">";
		out += "</label>";
		out += "<small class=\"error\">" + label + " field can only contain letters and numbers and cannot be empty</small>";
		out += "</div>";
		
		return out;		
	}
	
	public static String drawInputTextareaAlphanumeric(String field_name, String label, String placeholder, String value) {
		String out = "";
		
		out += "<div class=\"" + field_name + "\">";
		out += "<label>" + label + " <small>required</small>";
		out += "<textarea name=\"" + field_name + "\" class=\"class_" + field_name + "\" placeholder=\"" + placeholder + "\" required pattern=\"^[0-9a-zA-Z\\s\\r\\n]+$\">" + value + "</textarea>";
		out += "</label>";
		out += "<small class=\"error\">" + label + " field can only contain letters and numbers and cannot be empty</small>";
		out += "</div>";
		
		return out;		
	}


	public static String drawInputTextEmail(String field_name, String label, String placeholder, String value) {
		String out = "";
		
		out += "<div class=\"" + field_name + "\">";
		out += "<label>" + label + " <small>required</small>";
		out += "<input type=\"email\" name=\""+ field_name + "\" placeholder=\"" + placeholder + "\" value=\"" + value + "\" required \">";
		out += "</label>";
		out += "<small class=\"error\">" + label + " field can only contain letters and numbers and cannot be empty</small>";
		out += "</div>";
		
		return out;		
	}
	
	

	public static String drawInputTextareaReadonly(String field_name, String label, String placeholder, String value) {
		String out = "";

		out += "<div>";
		out += "<label>" + label + "</label>";
		out += "<textarea name=\"" + field_name + "\" class=\"class_" + field_name + "\" readonly>" + value + "</textarea>";
		out += "</div>";

		return out;
	}
	
	public static String drawStatusDropdownList(String currentStatus){
		String out="";
		out += "<a href=\"#\" data-dropdown=\"drop1\" class=\"button dropdown\">Status: "+currentStatus+"</a><br>";
		out += "<ul id=\"dropdownStatus\" data-dropdown-content class=\"f-dropdown\">";
		out += "<li>Activate</li>";
		out += "<li>Lock</li>";
		out += "</ul>";
		return out;
	}
}
