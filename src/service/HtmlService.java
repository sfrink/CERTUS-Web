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

	
	
	/**
	 * Dmitriy Karmazin
	 * This function generates HTML output for alphanumeric required text field
	 * @param field_name - name property for input type tag
	 * @param label - label will be visible to users
	 * @param placeholder - will go inside empty text field 
	 * @param value - will go inside text field as prefilled value
	 * @return
	 */	
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

	
	/**
	 * Dmitriy Karmazin
	 * This function generates HTML output for alphanumeric optional text field
	 * @param field_name - name property for input type tag
	 * @param label - label will be visible to users
	 * @param placeholder - will go inside empty text field 
	 * @param value - will go inside text field as prefilled value
	 * @return
	 */
	public static String drawInputTextAlphanumericOptional(String field_name, String label, String placeholder, String value) {
		String out = "";
		
		out += "<div class=\"" + field_name + "\">";
		out += "<label>" + label + " <small>optional</small>";
		out += "<input type=\"text\" name=\""+ field_name + "\" placeholder=\"" + placeholder + "\" value=\"" + value + "\" pattern=\"^[0-9a-zA-Z\\s\\r\\n]+$\"\">";
		out += "</label>";
		out += "<small class=\"error\">" + label + " field can only contain letters and numbers</small>";
		out += "</div>";
		
		return out;		
	}

	
	
	
	public static String drawInputTextareaAlphanumeric(String field_name, String label, String placeholder, String value, boolean error, String errorMessage) {
		String out = "", errorClass = "";
		String errorMessageToDisplay = label + " field can only contain letters and numbers and cannot be empty";
		
		if(error) {
			errorClass = "error";
			errorMessageToDisplay = errorMessage;
		}
		
		out += "<div class=\"" + field_name + " " + errorClass + "\">";
		out += "<label>" + label + " <small>required</small>";
		out += "<textarea name=\"" + field_name + "\" class=\"class_" + field_name + "\" placeholder=\"" + placeholder + "\" required pattern=\"^[0-9a-zA-Z\\s\\r\\n]+$\">" + value + "</textarea>";
		out += "</label>";
		out += "<small class=\"error\">" + errorMessageToDisplay + "</small>";
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
	
	public static String drawStatusDropdownList(String currentStatus, String field_name){
		String out="";
		out +="<div>";
		out += "<label>Current Status <small>required</small></label>";
		out += "<select name=\""+field_name+"\">";
		if(currentStatus.equals("Active")){
			out += "<option value=\"1\" selected>Activated</option>";
			out += "<option value=\"2\">Locked</option>";
		}
		else if(currentStatus.equals("Locked")){
			out += "<option value=\"1\">Activated</option>";
			out += "<option value=\"2\" selected>Locked</option>";
		}
		//out += "<option selected>Current Status: "+currentStatus+" </option>";
		out += "</select>";
		out += "</div>";
		/*out += "<a href=\"#\" data-dropdown=\"dropdownStatus\" class=\"button dropdown\">Status: "+currentStatus+"</a><br>";
		out += "<ul name = "+field_name+" id=\"dropdownStatus\" data-dropdown-content class=\"f-dropdown\">";
		out += "<li value=\"1\">Activate</li>";
		out += "<li value=\"2\">Lock</li>";
		out += "</ul>";*/
		return out;
	}
	
	
	
	
	public static String drawInputTextPassword(String field_name, String label, String placeholder, String value) {
		String out = "";

		out += "<div class=\"" + field_name + "\">";
		out += "<label>" + label + " <small>required</small>";
		out += "<input type=\"password\" name=\""+ field_name + "\" placeholder=\"" + placeholder + "\" value=\"" + value + "\" required pattern=\"[a-zA-Z]+\" \">";
		out += "</label>";
		out += "<small class=\"error\">" + label + " cannot be empty</small>";
		out += "</div>";
		
		return out;		
	}
	
	
	
	
	
	
	
}
