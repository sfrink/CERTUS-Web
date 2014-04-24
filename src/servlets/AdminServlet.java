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
import dto.Validator;
import enumeration.ElectionStatus;
import service.ElectionService;
import service.HeaderService;
import service.HtmlService;
import service.TallyingService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet(name = "adminElection", urlPatterns = { "/adminElection" })
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outModal = "";
	private String outElections = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (HeaderService.isTempUser(request)){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/inviteduser");		
			rd.forward(request, response);
		} else if(HeaderService.isAuthenticated(request)) {
			resetGlobals();
			routineExistingElections(request);
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/adminElection.jsp");
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
		if(HeaderService.isAuthenticated(request)) {
			resetGlobals();

			if (request.getParameter("delete") != null) {
				// SHOW DELETE ELECTION SCREEN
				routineDeleteElectionModal(request);
			} 
			else if(request.getParameter("confirm_delete") != null){
				routineDeleteElection(request);
			}
			// refresh existing elections
			routineExistingElections(request);

			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher(
					"/adminElection.jsp");
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
		this.outElections = "";
	}
	
	public void routineExistingElections(HttpServletRequest request) {
		// get the list of elections from DB
		ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();
		Validator v = ElectionService.selectElectionsForAdmin(request);
		
		if(v.isVerified()) {
			allElections = (ArrayList<ElectionDto>) v.getObject();	
		} else {
			messageAlert = HtmlService.drawMessageAlert(v.getStatus(), "alert") ;
		}
		
		outElections = drawExistingElections(allElections);
	}	
	
	public String drawExistingElections(ArrayList<ElectionDto> elections) {
		String out = "";
				
		if(elections != null && elections.size() != 0) {
			out += "<h5>Existing elections</h5>";
			out += "<form action=\"adminElection\" method=\"post\">";
			out += "<table><thead><tr>";
			out += "<th>Election Name</th>";
			out += "<th>Election Status</th>";
			out += "<th>Delete</th>";
			out += "</tr></thead><tbody>";
			
			for (ElectionDto e : elections) {
				String trClass=getElectionTableRowClass(e.getStatus());
				out += "<tr class =\"" + trClass + "\">";
				out += "<td class =\"" + trClass + "\">" + e.getElectionName() + "</td>";
				out += "<td class =\"" + trClass + "\">";
				out += drawElectionStatus(e.getStatus(), e.getStatusDescription()) + " ";
				out += "</td class =\"" + trClass + "\">";
				out += "<td class =\"" + trClass + "\">" + drawElectionDelete(e.getElectionId()) + "</td>";
				out += "</tr>";
			}
			
			out += "</tbody></table></form>";
		} else {
			out += "<div class=\"label secondary\">No elections exist yet</div>";
		}
		
		return out;
	}
	
	public String drawElectionStatus(int status, String value) {
		String out = "", outClass="";
		
		if(status == ElectionStatus.NEW.getCode()) {
			outClass = "label clear";
		} else if(status == ElectionStatus.OPEN.getCode()) {
			outClass = "label clear";
		} else if(status == ElectionStatus.CLOSED.getCode()) {
			outClass = "label clear";
		} else if(status == ElectionStatus.PUBLISHED.getCode()) {
			outClass = "label clear";
		} else {
			return out;
		}
		
		out = "<span class=\"" + outClass + "\">" + value + "</span>";		
		return out;
	}
	
	public String drawElectionStatusColored(int status, String value) {
		String out = "", outClass="";
		
		if(status == ElectionStatus.NEW.getCode()) {
			outClass = "";
		} else if(status == ElectionStatus.OPEN.getCode()) {
			outClass = "label success";
		} else if(status == ElectionStatus.CLOSED.getCode()) {
			outClass = "label alert";
		} else if(status == ElectionStatus.PUBLISHED.getCode()) {
			outClass = "label";
		} else {
			return out;
		}
		
		out = "<span class=\"" + outClass + "\">" + value + "</span>";		
		return out;
	}
	
	public String drawElectionDelete(int electionId) {
		String out = "<button class=\"label radius\" type=\"submit\" name=\"delete\" value=\"" + electionId + "\">delete</button></td>";
	
		return out;
	}
	
	public void routineDeleteElectionModal(HttpServletRequest request) {	
		resetGlobals();
		mode = "2";
		messageLabel = HtmlService.drawMessageLabel("", "secondary");
		ElectionDto e = new ElectionDto();
		e.setElectionId(Integer.parseInt(request.getParameter("delete")));
		outModal = drawDeleteElection(e);
	}
	
	public String drawDeleteElection(ElectionDto e) {
		String out = "";
		int valElecId = 0;
		// checking null case
		if(e != null) {
			valElecId = e.getElectionId();
		}
				
		out += "<h5>Are you sure you want to delete this election?</h5>";
		// button
		out += "<form action=\"adminElection\" method=\"post\">";
		out += "<div class=\"row\">";
		out += "<div class=\"large-3 large-centered medium-3 medium-centered columns\">";
		out += "<button class=\"radius button right\" type=\"submit\" name=\"confirm_delete\" value=\"" + valElecId + "\">Delete Election</button>";
		//out += "<button class=\"radius button right\" type=\"submit\" name=\"go_back\" value=\"" + valElecId + "\">Go Back</button>";
		out += "</div>";
		out += "</div>";
		out += "</form>";
		out += "<a class=\"close-reveal-modal\">&#215;</a>";

		return out;
	}

	public void routineDeleteElection(HttpServletRequest request) {
		resetGlobals();
		int elec_id=Integer.parseInt(request.getParameter("confirm_delete"));
		Validator vElection = ElectionService.disableElection(request, elec_id);

		if (vElection.isVerified()) {
			//delete successful
			mode = "1";
			messageAlert = HtmlService.drawMessageAlert(vElection.getStatus(), "success");
		} else {
			// errors, send back to add election screen
			mode = "2";
			messageLabel = HtmlService.drawMessageLabel(vElection.getStatus(), "alert");
			ElectionDto e=new ElectionDto();
			e.setElectionId(elec_id);
			outModal = drawDeleteElection(e);
		}
	}
	
	public String getElectionTableRowClass(int statusId) {
		String out = "";
		
		if(statusId == ElectionStatus.NEW.getCode()) {
			out += "election_new";
		} else if(statusId == ElectionStatus.OPEN.getCode()) {
			out += "election_open";
		} else if(statusId == ElectionStatus.CLOSED.getCode()) {
			out += "election_closed";
		} else if(statusId == ElectionStatus.PUBLISHED.getCode()) {
			out += "election_published";
		}
		
		return out;
	}
}
