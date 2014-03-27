package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rmi.Initializer;
import service.CandidateService;
import service.DrawHtmlService;
import service.ElectionService;
import service.HeaderService;

import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

import dto.CandidateDto;
import dto.ElectionDto;
import dto.Validator;
import enumeration.ElectionStatus;
import enumeration.Status;

/**
 * Servlet implementation class ElectionServlet
 */
@WebServlet(name = "election", urlPatterns = { "/election" })
public class ElectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mode = "1";
	private String messageAlert = "";
	private String messageLabel = "";
	private String outModal = "";
	private String outElections = "";

	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ElectionServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(HeaderService.isAuthenticated()) {
			// Draw Modal for new election
			mode = "1";
			messageAlert = "";
			messageLabel = "";
			outElections = "";
			outModal = "";

			// 1. get the list of all elections this user owns
			ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();
			Validator v = ElectionService.selectElectionsOwnedByUser(HeaderService.getUserId());
			
			if(v.isVerified()) {
				allElections = (ArrayList<ElectionDto>) v.getObject();	
			} else {
				messageAlert = DrawHtmlService.drawMessageAlert(v.getStatus(), "alert") ;
			}
			
			outElections = drawExistingElections(allElections);
			outModal = drawNewElection(null);
			
			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/election.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(HeaderService.isAuthenticated()) {
			// mode = "1";
			// messageAlert = "";
			// messageLabel = "";
			// outElections = "";
			// outModal = "";

			
			
			
			mode = "2";
			messageAlert = "";
			messageLabel = DrawHtmlService.drawMessageLabel(
					"Please fill in required labels", "secondary");
			String editMessage = "";

			ElectionDto newElection = new ElectionDto();
			ArrayList<CandidateDto> newCandidates = new ArrayList<CandidateDto>();
			ElectionDto editElection = null;
			ArrayList<CandidateDto> editElectionCandidates = null;

			// get information for election
			newElection.setElectionName(request
					.getParameter("new_election_name"));

			System.out.println("1: " + HeaderService.getUserId());
			newElection.setOwnerId(HeaderService.getUserId());
			System.out.println("2: " + newElection.getOwnerId());

			if (request.getParameter("election_id") != null) {
				newElection.setElectionId(Integer.parseInt(request
						.getParameter("election_id")));
			}

			// ADDING A NEW ELECTION
			if (request.getParameter("save_new_election") != null
					&& request.getParameter("save_new_election").equals(
							"Save Election")) {

				newCandidates = getCandidateListFromNames(request,
						"rowNewCandName", "rowNewCandIdHid");
				// save election with candidates
				newElection.setCandidateList(newCandidates);
				System.out.println("3" + newElection);

				Validator vElection = ElectionService
						.addElectionWithCandidates(newElection);

				if (vElection.isVerified()) {
					// insert of candidates was successful
					// change mode of the screen
					mode = "1";
					messageAlert = DrawHtmlService.drawMessageAlert(
							vElection.getStatus(), "success");

					newElection = null;
				} else {
					messageLabel = DrawHtmlService.drawMessageLabel(
							vElection.getStatus(), "alert");
				}
			}

			// EDIT EXISTING ELECTION
			if (request.getParameter("save_edited_election") != null
					&& request.getParameter("save_edited_election").equals(
							"Save Election")) {

				newCandidates = getCandidateListFromNames(request,
						"rowEditCandName", "rowEditCandIdHid");
				// save election with candidates
				newElection.setCandidateList(newCandidates);

				System.out.println("Inside editing");
				System.out.println(newElection.toString());

				// update existing election
				Validator vEditElection = ElectionService
						.editElectionWithCandidates(newElection);

				if (vEditElection.isVerified()) {
					// insert of candidates was successful
					// change mode of the screen
					mode = "1";
					messageAlert = DrawHtmlService.drawMessageAlert(
							vEditElection.getStatus(), "success");

					newElection = null;
				} else {
					messageLabel = DrawHtmlService.drawMessageLabel(
							vEditElection.getStatus(), "alert");
				}
			}

			// PERFORM ACTION ON ELECTION
			if (request.getParameter("button_election_action") != null) {
				int electionId = Integer.parseInt(request
						.getParameter("button_election_action"));
				Validator v = ElectionService.selectElection(electionId);

				if (v.isVerified()) {
					// get election object
					ElectionDto e = (ElectionDto) v.getObject();

					if (e.getStatus() == ElectionStatus.NEW.getCode()) {
						// open election
						Validator v2 = ElectionService.openElection(electionId);

						messageAlert = DrawHtmlService.drawMessageAlert(
								v2.getStatus(), "success");
					} else if (e.getStatus() == ElectionStatus.OPEN.getCode()) {
						// close election
						Validator v3 = ElectionService
								.closeElection(electionId);

						messageAlert = DrawHtmlService.drawMessageAlert(
								v3.getStatus(), "success");
					}
				}
			}

			if (request.getParameter("election") != null) {

				int electionId = Integer.parseInt(request
						.getParameter("election"));
				Validator vEditElection = ElectionService
						.selectElection(electionId);

				if (vEditElection.isVerified()) {
					Validator vEditElectionCandidates = CandidateService
							.selectCandidatesForElection(electionId,
									Status.ENABLED);

					if (vEditElectionCandidates != null
							&& vEditElectionCandidates.isVerified()) {
						editElection = (ElectionDto) vEditElection.getObject();
						editElectionCandidates = (ArrayList<CandidateDto>) vEditElectionCandidates
								.getObject();
						editElection.setCandidateList(editElectionCandidates);
						request.setAttribute("edit_election",
								drawNewElection(editElection));

						mode = "3";
					} else {
						editMessage = "1" + vEditElectionCandidates.getStatus();
					}
				} else {
					editMessage = "2" + vEditElection.getStatus();
				}
			} else {
				editMessage = "Incorrect election_id";
			}

			request.setAttribute("mode", mode);
			// request.setAttribute("existing_elections",
			// drawExistingElections());
			request.setAttribute("new_election", drawNewElection(newElection));
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);

			RequestDispatcher rd = getServletContext().getRequestDispatcher(
					"/election.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
			rd.forward(request, response);
		}
	}
	
	
	
	
	

	/**
	 * This function returns HTML output for all existing elections
	 * @return
	 */
	public String drawExistingElections(ArrayList<ElectionDto> elections) {
		String out = "";
		
		if(elections != null && elections.size() != 0) {
			out += "<h5>Existing elections</h5>";
			out += "<form action=\"election\" method=\"post\">";
			out += "<table><thead><tr>";
			out += "<th>ID</th>";
			out += "<th>Election Name</th>";
			out += "<th>Status</th>";
			out += "<th>Action</th>";
			out += "<th>Edit</th>";
			out += "</tr></thead><tbody>";
			
			int i = 1;
			for (ElectionDto e : elections) {
				out += "<tr>";
				out += "<td>" + e.getElectionId() + "</td>";
				out += "<td>" + e.getElectionName() + "</td>";
				out += "<td>" + e.getStatusDescription() + "</td>";
				out += "<td>" + drawElectionAction(e) + "</td>";
				out += "<td><button class=\"label secondary\" type=\"submit\" name=\"election\" value=\"" + e.getElectionId() + "\">edit</button></td>";
				out += "</tr>";
				i++;
			}
			
			out += "</tbody></table></form>";
			
		} else {
			out += "<div class=\"label secondary\">No elections exist yet</div>";
		}
		
		return out;
	}
	
	
	
	public String drawNewElection(ElectionDto e) {
		String out = "";
		// new candidate mode by default;
		String editingMode       = "save_new_election";
		String candRowId         = "rowNewCandId";
		String candRowIdHid		 = "rowNewCandIdHid";
		String candRowName       = "rowNewCandName";
		String candHolderId      = "rowNewCandHolder";
		String funcNewCandAdd    = "addNewCandRow";
		String funcNewCandRemove = "removeNewCandRow";

		if(e == null) {
			// if this election is new
			e = new ElectionDto();
			e.setElectionId(0);
			e.setElectionName("");
			// set at least one empty candidate
			ArrayList<CandidateDto> candidates = new ArrayList<CandidateDto>();
			CandidateDto c = new CandidateDto();
			candidates.add(c);
			e.setCandidateList(candidates);
		}
		
		// draw election info
		out += "<form action=\"election\" method=\"post\">";

		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		
		out += "<fieldset>";
		out += "<legend>Election Information</legend>";
		out += "<label>Election Name</label>";
		out += "<input type=\"text\" name=\"new_election_name\" value=\"" + e.getElectionName() + "\">";
		out += "</fieldset>";
		out += "</div>";

		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<div class=\"row\">";

		// draw candidates info
		out += "<fieldset>";
		out += "<legend>Add candidates</legend>";
		out += "<div id=\"" + candHolderId + "\">";
		
		int i = 1;
	    for (CandidateDto c : e.getCandidateList()) {
			out += "<div id=\"" + candRowId +  + i + "\">";
	    	out += "<label>Candidate Name</label>";
	    	out += "<input type=\"text\" name=\"" + candRowName + "\" value=\"" + c.getCandidateName() + "\" />";
	    	out += "<a class=\"button tiny radius alert\" href=\"javascript:void(0)\" onclick=\"" + funcNewCandRemove + "(" + i + ");\">Remove</a>";
	    	out += "</div>";

			i++;
	    }
		        
		out += "</div>";
		out += "<input onclick=\"" + funcNewCandAdd + "(this.form);\" type=\"button\" value=\"Add more candidates\" class=\"button tiny radius\" />";
		 
		out += "</fieldset>"; 
		out += "</div>"; 
		out += "</div>";		
		out += "</div>";

		out += "<div class=\"row\">";
		out += "<div class=\"large-2 large-centered medium-2 medium-centered columns\">";
		out += "<input type=\"submit\" name=\"" + editingMode + "\" class=\"small radius button left\" value=\"Save Election\">";
		out += "</div>";
		out += "</div>";
		
		out += "<input type=\"hidden\" name=\"election_id\" value=\"" + e.getElectionId() + "\">";
		out += "</form>";
		out += "<a class=\"close-reveal-modal\">&#215;</a>";

		return out;
	}


	
	
	public ArrayList<CandidateDto> getCandidateListFromNames(HttpServletRequest request, String candNames, String candIdsHid) {
		ArrayList<CandidateDto> newCandidates = new ArrayList<CandidateDto>();

		if(request.getParameterValues(candNames) != null) {
			String[] new_candidates_names = request.getParameterValues(candNames);

			String raw_candidate_id;
			int candidate_id;
			for(int i = 0; i < new_candidates_names.length; i++) {
				
				// determine candidate id if it was set
				candidate_id = 0;
				int j = i + 1;
				if(request.getParameter(candIdsHid+j) != null) {
					raw_candidate_id = (String) request.getParameter(candIdsHid+j);
					candidate_id = Integer.parseInt(raw_candidate_id);
				}
							
				CandidateDto c = new CandidateDto();
				c.setCandidateId(candidate_id);
				c.setCandidateName("" + new_candidates_names[i]);
				c.setDisplayOrder(i);
				c.setStatus(ElectionStatus.NEW.getCode());
				newCandidates.add(c);
			}
		}
		
		return newCandidates;	
	}
	
	public ArrayList<CandidateDto> getCandidateListFromId(HttpServletRequest request, String candNames, String candIdsHid) {
		ArrayList<CandidateDto> newCandidates = new ArrayList<CandidateDto>();

		if(request.getParameterValues(candNames) != null) {
			String[] new_candidates_names = request.getParameterValues(candNames);

			String raw_candidate_id;
			int candidate_id;
			for(int i = 0; i < new_candidates_names.length; i++) {
				
				// determine candidate id if it was set
				candidate_id = 0;
				int j = i + 1;
				if(request.getParameter(candIdsHid+j) != null) {
					raw_candidate_id = (String) request.getParameter(candIdsHid+j);
					candidate_id = Integer.parseInt(raw_candidate_id);
				}
							
				CandidateDto c = new CandidateDto();
				c.setCandidateId(candidate_id);
				c.setCandidateName("" + new_candidates_names[i]);
				c.setDisplayOrder(i);
				c.setStatus(ElectionStatus.NEW.getCode());
				newCandidates.add(c);
			}
		}
		
		return newCandidates;	
	}

	
	
	
	
	public String drawHiddenFieldsForCandidate(CandidateDto c) {
		String out = "";
		
		// check if the object is empty;
		if(c == null) {
			return out;
		}
		
		int candidateId = (c.getCandidateId() > 0) ? c.getCandidateId() : 0;
		
		out += "<input type=\"hidden\" name=\"candidate_ids[]\" value=\"" + c.getCandidateId() + "\">";
		out += "<input type=\"hidden\" name=\"candidate_status" + candidateId + "\" value=\"" + c.getStatus() + "\">";
		
		return out;
	}
	
	public String drawElectionAction(ElectionDto e) {
		String out = "";
		String actionName = "button_election_action";
		String actionClass = "";
		int actionId = e.getElectionId();
		String actionTargetStatusName = "";
		
		if(e == null) {
			return out;
		}
		
		if(e.getStatus() == ElectionStatus.NEW.getCode()) {
			actionTargetStatusName = ElectionStatus.OPEN.getLabel();
			actionClass="success";
			out = "<button class=\"label " + actionClass + " \" type=\"submit\" name=\"" + actionName + "\" value=\"" + actionId + "\">" + actionTargetStatusName + "</button>";
		} else if(e.getStatus() == ElectionStatus.OPEN.getCode()) {
			actionTargetStatusName = ElectionStatus.CLOSED.getLabel();
			actionClass="alert";
			out = "<button class=\"label " + actionClass + " \" type=\"submit\" name=\"" + actionName + "\" value=\"" + actionId + "\">" + actionTargetStatusName + "</button>";
		} else if(e.getStatus() == ElectionStatus.CLOSED.getCode()) {
			out = "<span class=\"label\">Closed</span>";
		} else if(e.getStatus() == ElectionStatus.PUBLISHED.getCode()) {
			out = "<span class=\"label\">Results Published</span>";
		}

		return out;
	}

	
	
	
	public String drawEditElection(ElectionDto e) {
		String out = "";
		// new candidate mode by default;
		String editingMode       = "save_new_election";
		String candRowId         = "rowNewCandId";
		String candRowIdHid		 = "rowNewCandIdHid";
		String candRowName       = "rowNewCandName";
		String candHolderId      = "rowNewCandHolder";
		String funcNewCandAdd    = "addNewCandRow";
		String funcNewCandRemove = "removeNewCandRow";

		int electionId = 0;
		
		ArrayList<CandidateDto> candidates =  new ArrayList<CandidateDto>();

		if(e == null) {
			e = new ElectionDto();
			e.setCandidateList(candidates);
		} else {
			candidates = e.getCandidateList();
		}
		
		if(e.getElectionName() == null)
			e.setElectionName("");
		
		if(e.getElectionId() > 0) {
			// editing existing candidate mode
			editingMode       = "save_edited_election";
			candRowId         = "rowEditCandId";
			candRowIdHid      = "rowEditCandIdHid";
			candRowName       = "rowEditCandName";
			candHolderId      = "rowEditCandHolder";
			funcNewCandAdd    = "addEditCandRow";
			funcNewCandRemove = "removeEditCandRow";
			electionId = e.getElectionId();
		}
		
		
		// draw election info
		out += "<form action=\"election\" method=\"post\">";

		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		
		out += "<fieldset>";
		out += "<legend>Election Information</legend>";
		out += "<label>Election Name</label>";
		out += "<input type=\"text\" name=\"new_election_name\" value=\"" + e.getElectionName() + "\">";
		out += "</fieldset>";
		out += "</div>";

		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<div class=\"row\">";

		// draw candidates info
		out += "<fieldset>";
		out += "<legend>Add candidates</legend>";
		out += "<div id=\"" + candHolderId + "\">";
		
		int i = 1;
	    for (CandidateDto c : candidates) {
	    	
			out += "<div id=\"" + candRowId +  + i + "\">";
	    	out += "<label>Candidate Name</label>";
	    	if(c.getCandidateId() > 0) {
	    		out += drawHiddenFieldsForCandidate(c);
	    	}
	    	out += "<input type=\"text\" name=\"" + candRowName + "\" value=\"" + c.getCandidateName() + "\" />";
	    	out += "<a class=\"button tiny radius alert\" href=\"javascript:void(0)\" onclick=\"" + funcNewCandRemove + "(" + i + ");\">Remove</a>";
	    	out += "</div>";

			i++;
	    }
		           
		if(candidates.size() == 0) {
			out += "<label>Candidate Name</label>";
			out += "<input type=\"text\" name=\"" + candRowName + "\" value=\" \" />";
		}
		        
		out += "</div>";
		out += "<input onclick=\"" + funcNewCandAdd + "(this.form);\" type=\"button\" value=\"Add more candidates\" class=\"button tiny radius\" />";
		 
		out += "</fieldset>"; 
		out += "</div>"; 
		out += "</div>";		
		out += "</div>";

		out += "<div class=\"row\">";
		out += "<div class=\"large-2 large-centered medium-2 medium-centered columns\">";
		out += "<input type=\"submit\" name=\"" + editingMode + "\" class=\"small radius button left\" value=\"Save Election\">";
		out += "</div>";
		out += "</div>";
		
		out += "<input type=\"hidden\" name=\"election_id\" value=\"" + electionId + "\">";
		out += "</form>";
		out += "<a class=\"close-reveal-modal\">&#215;</a>";

		return out;
	}

	
	
	
	
	
	
	
	
}

