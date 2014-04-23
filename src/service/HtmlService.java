package service;

import enumeration.UserStatus;

public class HtmlService {

	
	/**
	 * Dmitriy Karmazin
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
	 * Dmitriy Karmazin
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

	
	
	
	public static String drawInputTextareaAlphanumeric(String field_name, String label, String placeholder, String value, boolean error, String errorMessage, boolean required) {
		String out = "", errorClass = "";
		String outRequired = required ? "required" : "";
		String errorMessageToDisplay = label + " field can only contain letters and numbers and cannot be empty";
		
		
		if(error) {
			errorClass = "error";
			errorMessageToDisplay = errorMessage;
		}
		
		out += "<div class=\"" + field_name + " " + errorClass + "\">";
		out += "<label>" + label + " <small>required</small>";
		out += "<textarea id=\"" + field_name + "\" name=\"" + field_name + "\" class=\"class_" + field_name + "\" placeholder=\"" + placeholder + "\" " + outRequired + " pattern=\"^[0-9a-zA-Z@.\\s\\r\\n]+$\">" + value + "</textarea>";
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
	
	public static String drawInputTextEmailForgot(String field_name, String label, String placeholder, String value, String errorMessage) {
		String out="";
		String errorMessageToDisplay = errorMessage;
		
		
		out += "<div class=\"" + field_name + " error\">";
		out += "<label>" + label + " <small>required</small>";
		out += "<input type=\"email\" name=\""+ field_name + "\" placeholder=\"" + placeholder + "\" value=\"" + value + "\" required \">";
		out += "</label>";
		out += "<small class=\"error\">" + errorMessageToDisplay + "</small>";
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
	
	public static String drawStatusDropdownList(int currentStatus, String field_name){
		String out="";
		out +="<div>";
		out += "<label>Current Status <small>required</small></label>";
		out += "<select name=\""+field_name+"\">";
		if(currentStatus == UserStatus.ACTIVE.getCode()){
			out += "<option value=\"1\" selected>Activated</option>";
			out += "<option value=\"2\">Locked</option>";
		}
		else if(currentStatus == UserStatus.LOCKED.getCode()){
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
	
	
	
	
	public static String drawInputTextPassword(String field_name, String label, String placeholder, String value, boolean error, String errorMessage) {
		String out = "", errorClass = "";
		String errorMessageToDisplay = label + " cannot be empty";

		if(error) {
			errorClass = "error";
			errorMessageToDisplay = errorMessage;
		}

		out += "<div class=\"" + field_name + " " + errorClass + "\">";
		out += "<label>" + label + " <small>Required</small>";
		out += "<input type=\"password\" name=\""+ field_name + "\" placeholder=\"" + placeholder + "\" value=\"" + value + "\" required pattern=\"[a-zA-Z]+\" \">";
		out += "</label>";
		out += "<small class=\"error\">" + errorMessageToDisplay + "</small>";
		out += "</div>";
		
		return out;		
	}
	
	public static String drawInputTextPasswordAndConfirmation(String pField_name, String pLabel, String cField_name, String cLabel) {
		String out = "";
		
		//Password Field:
		out += "<div class=\"" + pField_name + "\">";
		out += "<label>" + pLabel + " <small>Required</small>";
		out += "<input type=\"password\" name=\""+ pField_name + "\" placeholder=\"" + pLabel + "\" id=\"" + pField_name + "\" required pattern=\"[a-zA-Z]+\">";
		out += "</label>";
		out += "<small class=\"error\">Your password must match the requirements</small>";
		out += "</div>";

		//Confirmation Field
		out += "<div class=\"" + cField_name + "\">";
		out += "<label>" + cLabel + " <small>Required</small>";
		out += "<input type=\"password\" name=\""+ cField_name + "\" placeholder=\"" + cLabel + "\" id=\"" + cField_name + "\" required pattern=\"[a-zA-Z]+\" data-equalto=\""+ pField_name +"\">";
		out += "</label>";
		out += "<small class=\"error\">Passwords don't match</small>";
		out += "</div>";

		return out;
	}
	
	public static String drawSelectBoxElectionPrivateOrPublic(String field_name, int selectedId) {
		String out = "", selected1 = "", selected2 = "";

		if(selectedId == 1) {
			selected1 = "selected";
		} else if(selectedId == 2) {
			selected2 = "selected";
		}
		
		out += "<div class=\"" + field_name + "\">";
		out += "<label>Who can vote <small>required</small>";
        out += "<select id=\"" + field_name + "\" name=\"" + field_name + "\" required pattern=\"[12]\">";
        out += "<option value=\"2\""+ selected2 + ">Only specified users can vote (private election)</option>";
        out += "<option value=\"1\""+ selected1 + ">Any user can vote (public election)</option>";
        out += "</select>";
        out += "</label>";
        out += "<small class=\"error\">please select either public or private type of election</small>";
        out += "</div>";
		
		return out;
	}
	
	
	public static String drawCheckBoxesElectionPrivateOrPublic(String field_name, boolean errorFlag, String emails) {
		String out = "";
		String delimiter = System.getProperty("line.separator");

		if(errorFlag) {
			out += "<p>Check emails of users you would like to invite</p>";

			String[] emailsArr = emails.split(delimiter);
			for(int i = 0; i < emailsArr.length; i++) {
              out += "<div>";
			  out += "<input id=\"" + emailsArr[i] + "\" name=\"" + field_name + "\" value=\"" + emailsArr[i] + "\" type=\"checkbox\">";
			  out += "<label for=\"" + emailsArr[i] + "\">" + emailsArr[i] + "</label>";
              out += "</div>";
			}
		}
		
		return out;
	}
	
	
}
