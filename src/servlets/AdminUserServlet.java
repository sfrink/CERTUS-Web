package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.ElectionDto;
import dto.ElectionProgressDto;
import dto.UserDto;
import dto.Validator;
import enumeration.ElectionStatus;
import enumeration.UserStatus;
import service.ElectionService;
import service.HeaderService;
import service.HtmlService;
import service.TallyingService;
import service.UserService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet(name = "adminUser", urlPatterns = { "/adminUser" })
public class AdminUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outModal = "";
	private String outUsers = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(HeaderService.isAuthenticated()) {
			resetGlobals();
			routineExistingUsers();
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_users", outUsers);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/adminUser.jsp");
			rd.forward(request, response);		
		}
		else{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(HeaderService.isAuthenticated()) {
			resetGlobals();

			if (request.getParameter("edit") != null) {
				// SHOW EDIT USER SCREEN
				routineEditUserModal(request);
			} 
			else if(request.getParameter("save_edit_user") != null){
				routineEditUser(request);
			}
			// refresh 
			routineExistingUsers();

			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_users", outUsers);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher(
					"/adminUser.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	
	}
	
	private void resetGlobals() {
		this.mode = "1";
		this.messageAlert = "";
		this.messageLabel = "";
		this.outModal = "";
		this.outUsers = "";
	}
	
	public void routineExistingUsers() {
		// get the list of elections from DB
		ArrayList<UserDto> allUsers = new ArrayList<UserDto>();
		Validator v = UserService.selectAllUsers();
		
		if(v.isVerified()) {
			allUsers = (ArrayList<UserDto>) v.getObject();	
		} else {
			messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert") ;
		}
		
		outUsers = drawExistingUsers(allUsers);
	}	
	
	public String drawExistingUsers(ArrayList<UserDto> users) {
		String out = "";
				
		if(users != null && users.size() != 0) {
			out += "<h5>Existing users</h5>";
			out += "<form action=\"adminUser\" method=\"post\">";
			out += "<table><thead><tr>";
			out += "<th>User ID</th>";
			out += "<th>User Name</th>";
			out += "<th>User Email</th>";
			out += "<th>Status</th>";
			out += "<th>Edit</th>";
			out += "</tr></thead><tbody>";
			
			for (UserDto u : users) {
				
				out += "<tr>";
				out += "<td>"+ u.getUserId() + "</td>";
				out += "<td>" + u.getFirstName() +" "+u.getLastName()+ "</td>";
				out += "<td>" + u.getEmail() + "</td>";
				out += "<td>"+ drawUserStatusColored(u.getStatus(), u.getStatusDescription()) + " </td>";
				out += "<td>" + drawUserEdit(u.getUserId()) + "</td>";
				out += "</tr>";
			}
			
			out += "</tbody></table></form>";
		} else {
			out += "<div class=\"label secondary\">No elections exist yet</div>";
		}
		
		return out;
	}
	
	public String drawUserStatusColored(int status, String value) {
		String out = "", outClass="";
		
		if(status == UserStatus.ACTIVE.getCode()) {
			outClass = "";
		} else if(status == UserStatus.LOCKED.getCode()) {
			outClass = "label success";
		}
		
		out = "<span class=\"" + outClass + "\">" + value + "</span>";		
		return out;
	}
	
	public String drawUserEdit(int userId) {
		String out = "<button class=\"label radius\" type=\"submit\" name=\"edit\" value=\"" + userId + "\">edit</button></td>";
	
		return out;
	}
	
	public void routineEditUserModal(HttpServletRequest request) {	
		resetGlobals();
		mode = "2";
		messageLabel = HtmlService.drawMessageLabel("", "secondary");
		UserDto u = new UserDto();
		u.setUserId(Integer.parseInt(request.getParameter("edit")));
		outModal = drawEditUser(u);
	}
	
	public String drawEditUser(UserDto u) {
		String out = "";
		int valUserId = 0;
		int valUserStatus=0;
		String valUserFirstName = "", valUserLastName="", valUserEmail="";
		// checking null case
		if(u != null) {
			valUserId = u.getUserId();
			valUserFirstName = u.getFirstName();
			valUserLastName = u.getLastName();
			valUserEmail=u.getEmail();
			valUserStatus=u.getStatus();	
		}

		out += "<h5>Edit user</h5>";
		out += "<form id=\"form_user_edit\" action=\"adminUser\" method=\"post\" data-abide>";
		out += "<div class=\"row\">";
		// draw election info
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<fieldset>";
		out += "<legend>User Information</legend>";
		out += HtmlService.drawInputTextAlphanumeric("edit_user_first_name", "First Name", "Enter First Name", valUserFirstName);
		out += HtmlService.drawInputTextAlphanumeric("edit_user_last_name", "Last Name", "Enter Last Name", valUserLastName);
		out += HtmlService.drawInputTextareaAlphanumeric("edit_user_email", "Email", "Enter Email", valUserEmail);
		out += HtmlService.drawInputTextAlphanumeric("edit_user_status", "Status", "Enter Status", ""+valUserStatus);
		out += "</fieldset>";
		out += "</div>";
		// button
		out += "<div class=\"row\">";
		out += "<div class=\"large-3 large-centered medium-3 medium-centered columns\">";
		out += "<button class=\"radius button right\" type=\"submit\" name=\"save_edit_user\" value=\"" + valUserId + "\">Save User</button>";
		out += "</div>";
		out += "</div>";
		out += "</form>";
		out += "<a class=\"close-reveal-modal\">&#215;</a>";

		return out;
	}

	public void routineEditUser(HttpServletRequest request) {
		resetGlobals();
		UserDto u=new UserDto();
		u.setUserId(Integer.parseInt(request.getParameter("save_edit_user")));
		u.setFirstName(request.getParameter("edit_user_first_name"));
		u.setLastName(request.getParameter("edit_user_last_name"));
		u.setEmail(request.getParameter("edit_user_email"));
		u.setStatus(Integer.parseInt(request.getParameter("edit_user_status")));
		Validator vUser = UserService.editUser(u);

		if (vUser.isVerified()) {
			//edit successful
			mode = "1";
			messageAlert = HtmlService.drawMessageAlert(vUser.getStatus(), "success");
		} else {
			// errors, send back to edit screen
			mode = "2";
			messageLabel = HtmlService.drawMessageLabel(vUser.getStatus(), "alert");
			UserDto user=new UserDto();
			user.setUserId(Integer.parseInt(request.getParameter("save_edit_user")));
			outModal = drawEditUser(user);
		}
	}
}
