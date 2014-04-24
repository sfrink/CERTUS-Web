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
			v1 = Initializer.rmi.selectElectionsForVoter(userId, sessionID);
		} catch (RemoteException e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
		}
		return v1;
	}
	
	
	public static Validator saveVote(VoteDto vote) {
		Validator v1 = new Validator();
		
		v1.setVerified(false);
		v1.setStatus("failed!");
		
		
		
		return v1;
	}
	
	
	public static Validator selectElectionForOwner(HttpServletRequest request, int id) {
		Validator v1 = new Validator();
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			v1 = Initializer.rmi.selectElectionForOwner(id, sessionID);
		} catch (RemoteException e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
		}
		return v1;
	}

	public static Validator selectElectionForVoter(HttpServletRequest request, int id) {
		Validator v1 = new Validator();
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			v1 = Initializer.rmi.selectElectionForVoter(id, sessionID);
		} catch (RemoteException e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
		}
		return v1;
	}
	
	
	public static Validator deleteElection(HttpServletRequest request, int electionID){
		Validator val=new Validator();
		try{
			String sessionID = HeaderService.getUserSessionId(request);
			val=Initializer.rmi.deleteElection(electionID, sessionID);
		}
		catch (RemoteException e){
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	
	public static Validator selectElectionFullDetail(HttpServletRequest request, int electionId) {
		Validator validator = null;
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.selectElectionFullDetail(electionId, sessionID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}
	
    
	public static Validator selectElectionsForAdmin(HttpServletRequest request) {
    	Validator validator = null;
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.selectElectionsForAdmin(sessionID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    

    
    public static Validator selectElectionsForOwner(HttpServletRequest request, int electionOwnerId) {
    	
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.selectElectionsForOwner(electionOwnerId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    

    
    
    public static Validator addElection(HttpServletRequest request, ElectionDto electionDto)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.addElection(electionDto, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator addAdditionalUsersToElection(HttpServletRequest request, ElectionDto electionDto)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.addAdditionalUsersToElection(electionDto, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator editElection(HttpServletRequest request, ElectionDto election)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.editElection(election, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator openElection(HttpServletRequest request, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.openElectionAndPopulateCandidates(electionId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator reOpenElection(HttpServletRequest request, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.OPEN, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator closeElection(HttpServletRequest request, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.CLOSED, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator publishResults(HttpServletRequest request, int electionId, String password){
    	Validator val=new Validator();
    	
    	try{
			String sessionID = HeaderService.getUserSessionId(request);
    		val=Initializer.rmi.publishResults(electionId, password, sessionID);
    	} catch(RemoteException e){
    		e.printStackTrace();
    	}
    	return val;
    }
    
    public static Validator disableElection(HttpServletRequest request, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.deleteElection(electionId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }


    
    public static Validator selectElectionsForVotingForUser(HttpServletRequest request, int userId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.selectElectionsForVoter(userId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }
    
    
    
    public static Validator selectElectionsForResults(HttpServletRequest request, int userId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.selectElectionsForResults(userId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }

    
    public static Validator selectResults(HttpServletRequest request, int electionId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator  = Initializer.rmi.selectResults(electionId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }
}
