package service;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import rmi.Initializer;
import dto.ElectionDto;
import dto.Validator;
import dto.VoteDto;
import enumeration.ElectionStatus;

public class ElectionService {
	
	public static Validator selectElectionsForVoter(HttpServletRequest request, int userId) {
		Validator v1 = new Validator();
		String sessionID = HeaderService.getUserSessionId(request);

		try {
			v1 = Initializer.getRmi().selectElectionsForVoter(userId, sessionID);
		} catch (Exception e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return v1;
	}
	
	public static Validator selectElectionForOwner(HttpServletRequest request, int id) {
		Validator v1 = new Validator();
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			v1 = Initializer.getRmi().selectElectionForOwner(id, sessionID);
		} catch (Exception e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return v1;
	}

	public static Validator selectElectionForVoter(HttpServletRequest request, int id) {
		Validator v1 = new Validator();
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			v1 = Initializer.getRmi().selectElectionForVoter(id, sessionID);
		} catch (Exception e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return v1;
	}
	
	
	public static Validator deleteElection(HttpServletRequest request, int electionID){
		Validator val=new Validator();
		try{
			String sessionID = HeaderService.getUserSessionId(request);
			val=Initializer.getRmi().deleteElection(electionID, sessionID);
		} catch (Exception e) {
			val.setVerified(false);
			val.setStatus("RMI call failed");
			HeaderService.errorLogout(request);
		}
		return val;
	}
	
	
	public static Validator selectElectionFullDetail(HttpServletRequest request, int electionId) {
		Validator validator = null;
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().selectElectionFullDetail(electionId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
	}
	
    
	public static Validator selectElectionsForAdmin(HttpServletRequest request) {
    	Validator validator = null;
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().selectElectionsForAdmin(sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }
    

    
    public static Validator selectElectionsForOwner(HttpServletRequest request, int electionOwnerId) {
    	
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().selectElectionsForOwner(electionOwnerId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }
    

    
    
    public static Validator addElection(HttpServletRequest request, ElectionDto electionDto)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().addElection(electionDto, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }
    
    public static Validator addAdditionalUsersToElection(HttpServletRequest request, ElectionDto electionDto)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().addAdditionalUsersToElection(electionDto, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }
    
    public static Validator editElection(HttpServletRequest request, ElectionDto election)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().editElection(election, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }
    
    public static Validator openElection(HttpServletRequest request, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().openElectionAndPopulateCandidates(electionId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }
    
    public static Validator reOpenElection(HttpServletRequest request, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().editElectionStatus(electionId, ElectionStatus.OPEN, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }
    
    public static Validator closeElection(HttpServletRequest request, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().editElectionStatus(electionId, ElectionStatus.CLOSED, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }
    
    public static Validator publishResults(HttpServletRequest request, int electionId, String password){
    	Validator val=new Validator();
    	
    	try{
			String sessionID = HeaderService.getUserSessionId(request);
    		val=Initializer.getRmi().publishResults(electionId, password, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
    	return val;
    }
    
    public static Validator disableElection(HttpServletRequest request, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().deleteElection(electionId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
    }


    
    public static Validator selectElectionsForVotingForUser(HttpServletRequest request, int userId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().selectElectionsForVoter(userId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
    	
		return validator;  
    }
    
    
    
    public static Validator selectElectionsForResults(HttpServletRequest request, int userId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().selectElectionsForResults(userId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
    	
		return validator;  
    }

    
    public static Validator selectResults(HttpServletRequest request, int electionId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.getRmi().selectResults(electionId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
    	
		return validator;  
    }
}
