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
import service.TallyingService;
import service.VotingService;

import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

import dto.CandidateDto;
import dto.ElectionDto;
import dto.ElectionProgressDto;
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
			mode = "1";
			messageAlert = "";
			messageLabel = "";
			outElections = "";
			outModal = "";

			
			// ADD NEW ELECTION BUTTON PRESSED
			if (request.getParameter("button_add_election") != null
					&& request.getParameter("button_add_election").equals(
							"new")) {
				mode = "2";
				messageAlert = "";
				messageLabel = DrawHtmlService.drawMessageLabel("Please fill in required labels", "secondary");
				outModal = drawNewElection(null);
			}

			
			// ADDING A NEW ELECTION
			if (request.getParameter("save_new_election") != null
					&& request.getParameter("save_new_election").equals(
							"Save Election")) {
				
				ElectionDto newElection = new ElectionDto();
				ArrayList<CandidateDto> newCandidates = new ArrayList<CandidateDto>();
				// get information for election
				newElection.setOwnerId(0);
				newElection.setElectionName(request
						.getParameter("new_election_name"));
				newElection.setOwnerId(HeaderService.getUserId());
				newCandidates = getCandidateListFromNames(request,
						"rowNewCandName", "rowNewCandIdHid"); // get candidates list
				newElection.setCandidateList(newCandidates); // save candidates list

				Validator vElection = ElectionService
						.addElectionWithCandidates(newElection);

				if (vElection.isVerified()) {
					// insert of candidates was successful
					mode = "1";
					messageAlert = DrawHtmlService.drawMessageAlert(
							vElection.getStatus(), "success");
					messageLabel = "";
					outModal = "";
				} else {
					// send the user back to screen
					mode = "2";
					messageAlert = "";
					messageLabel = DrawHtmlService.drawMessageLabel(
							vElection.getStatus(), "alert");
					outModal = drawNewElection(newElection);
				}
			}
			
			
			// SHOW ELECTION FOR EDITING
			if (request.getParameter("election") != null) {
				int electionId = Integer.parseInt(request
						.getParameter("election"));

				ElectionDto editElection = null;
				ArrayList<CandidateDto> editElectionCandidates = null;
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

						mode = "2";
						messageAlert = "";
						messageLabel = "";
						outModal = drawEditElection(editElection);
					} else {
						DrawHtmlService.drawMessageAlert(vEditElectionCandidates.getStatus(), "alert");
					}
				} else {
					DrawHtmlService.drawMessageAlert(vEditElection.getStatus(), "alert");
				}
			} else {
				DrawHtmlService.drawMessageAlert("Incorrect election_id", "alert");
			}

			
			// SAVE EDITED ELECTION
			if (request.getParameter("save_edited_election") != null
					&& request.getParameter("save_edited_election").equals(
							"Save Election")) {
				
				ElectionDto editElection = new ElectionDto();
				editElection.setElectionId(Integer.parseInt(request.getParameter("election_id")));
				editElection.setOwnerId(HeaderService.getUserId());
				editElection.setElectionName(request.getParameter("new_election_name"));
				ArrayList<CandidateDto> editElectionCandidates = new ArrayList<CandidateDto>();
				editElectionCandidates = getCandidateListFromId(request);
				// save election with candidates
				editElection.setCandidateList(editElectionCandidates);

				// update existing election
				Validator vEditElection = ElectionService
						.editElectionWithCandidates(editElection);

				if (vEditElection.isVerified()) {
					// insert of candidates was successful
					mode = "1";
					messageAlert = DrawHtmlService.drawMessageAlert(
							vEditElection.getStatus(), "success");
					messageLabel = "";
					outModal = "";
				} else {
					mode = "2";
					messageAlert = "";
					messageLabel = DrawHtmlService.drawMessageLabel(
							vEditElection.getStatus(), "alert");
					outModal = drawEditElection(editElection);
				}
			}
			
			
			// PERFORM ACTION ON ELECTION
			if (request.getParameter("button_election_action") != null) {
				int electionId = Integer.parseInt(request
						.getParameter("button_election_action"));
				Validator v1 = ElectionService.selectElection(electionId);

				if (v1.isVerified()) {
					// get election object
					ElectionDto e = (ElectionDto) v1.getObject();

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

			
			// 1. get the list of all elections this user owns
			ArrayList<ElectionDto> allElections = new ArrayList<ElectionDto>();
			Validator v = ElectionService.selectElectionsOwnedByUser(HeaderService.getUserId());
			
			if(v.isVerified()) {
				allElections = (ArrayList<ElectionDto>) v.getObject();	
			} else {
				messageAlert = DrawHtmlService.drawMessageAlert(v.getStatus(), "alert") ;
			}
			
			outElections = drawExistingElections(allElections);
			

			request.setAttribute("mode", mode);
			request.setAttribute("message_alert", messageAlert);
			request.setAttribute("message_label", messageLabel);
			request.setAttribute("out_elections", outElections);
			request.setAttribute("out_modal", outModal);
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
			out += "<th>Voting Progress</th>";
			out += "</tr></thead><tbody>";
			
			int i = 1, voted = -1;
			for (ElectionDto e : elections) {
				
				Validator v2 = TallyingService.voteProgressStatusForElection(e.getElectionId());
				if(v2.isVerified()) {
					ElectionProgressDto epd = (ElectionProgressDto) v2.getObject();
					voted = epd.getTotalVotes();
				}
				
				out += "<tr>";
				out += "<td>" + e.getElectionId() + "</td>";
				out += "<td>" + e.getElectionName() + "</td>";
				out += "<td>" + e.getStatusDescription() + "</td>";
				out += "<td>" + drawElectionAction(e) + "</td>";
				out += "<td><button class=\"label secondary\" type=\"submit\" name=\"election\" value=\"" + e.getElectionId() + "\">edit</button></td>";
				out += "<td>"+ voted + "</td>";
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
			c.setCandidateName("");
			candidates.add(c);
			e.setCandidateList(candidates);
		}
		
		// draw election info
		out += "<h5>Add new election</h5>";
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
	
	public ArrayList<CandidateDto> getCandidateListFromId(HttpServletRequest request) {
		ArrayList<CandidateDto> newCandidates = new ArrayList<CandidateDto>();

		// existing records
		if(request.getParameterValues("edit_candidate_ids") != null) {
			String[] existing_cand_names = request.getParameterValues("edit_candidate_ids");
			
			for(String cand: existing_cand_names) {
				// for all existing candidates
				CandidateDto c = new CandidateDto();
				// set id
				c.setCandidateId(Integer.parseInt(cand));

				// set name
				if(request.getParameter("edit_candidate_name" + cand) != null) {
					c.setCandidateName(request.getParameter("edit_candidate_name" + cand));
				}

				// set status				
				if(request.getParameter("edit_candidate_status" + cand) != null) {
					c.setStatus(Integer.parseInt(request.getParameter("edit_candidate_status" + cand)));				
				}

				// set display order
				c.setDisplayOrder(0);
				// add to list
				newCandidates.add(c);
			}
		}
		
		// new records
		if(request.getParameterValues("rowEditCandNameNew") != null) {
			String[] new_cand_names = request.getParameterValues("rowEditCandNameNew");

			for(int i = 0; i < new_cand_names.length; i++) {
				CandidateDto c = new CandidateDto();
				c.setCandidateId(0);
				c.setCandidateName("" + new_cand_names[i]);
				c.setDisplayOrder(i);
				c.setStatus(ElectionStatus.NEW.getCode());
				newCandidates.add(c);
			}
		}
		
		return newCandidates;	
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

		String editingMode       = "save_edited_election";
		String candRowId         = "rowEditCandId";
		String candRowIdHid      = "rowEditCandIdHid";
		String candRowName       = "rowEditCandName";
		String candHolderId      = "rowEditCandHolder";
		String funcNewCandAdd    = "addEditCandRow";
		String funcNewCandRemove = "removeEditExistingCandRow";
		
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
		
		
	    for (CandidateDto c : e.getCandidateList()) {	    	
			out += "<div id=\"" + candRowId + c.getCandidateId() + "\">";
	    	out += "<label>Candidate Name</label>";
	    	
			out += "<input type=\"hidden\" name=\"edit_candidate_ids\" value=\"" + c.getCandidateId() + "\">";
			out += "<input type=\"hidden\" name=\"edit_candidate_status" + c.getCandidateId() + "\" value=\"" + c.getStatus() + "\">";
	    	out += "<input type=\"text\" name=\"edit_candidate_name" + c.getCandidateId() + "\" value=\"" + c.getCandidateName() + "\" />";

	    	out += "<a class=\"button tiny radius alert\" href=\"javascript:void(0)\" onclick=\"" + funcNewCandRemove + "(" + c.getCandidateId() + ");\">Remove</a>";
	    	out += "</div>";
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

	
	
	
	
	
	
	
	
}

