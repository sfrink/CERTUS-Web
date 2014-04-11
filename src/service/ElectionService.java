package service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.Initializer;
import dto.CandidateDto;
import dto.ElectionDto;
import dto.Validator;
import dto.VoteDto;
import enumeration.ElectionStatus;
import enumeration.Status;

public class ElectionService {
	
	public static Validator selectElectionsForVoter(int userId) {
		Validator v1 = new Validator();
		String sessionID = HeaderService.getUserSessionId();

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
	
	
	public static Validator selectElectionForOwner(int id) {
		Validator v1 = new Validator();
		try {
			String sessionID = HeaderService.getUserSessionId();
			v1 = Initializer.rmi.selectElectionForOwner(id, sessionID);
		} catch (RemoteException e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
		}
		return v1;
	}

	public static Validator selectElectionForVoter(int id) {
		Validator v1 = new Validator();
		try {
			String sessionID = HeaderService.getUserSessionId();
			v1 = Initializer.rmi.selectElectionForVoter(id, sessionID);
		} catch (RemoteException e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
		}
		return v1;
	}
	
	
	public static Validator deleteElection(int electionID){
		Validator val=new Validator();
		try{
			String sessionID = HeaderService.getUserSessionId();
			val=Initializer.rmi.deleteElection(electionID, sessionID);
		}
		catch (RemoteException e){
			val.setVerified(false);
			val.setStatus("RMI call failed");
		}
		return val;
	}
	
	
	public static Validator selectElectionFullDetail(int electionId) {
		Validator validator = null;
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.selectElectionFullDetail(electionId, sessionID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}
	
    
	public static Validator selectElectionsForAdmin() {
    	Validator validator = null;
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.selectElectionsForAdmin(sessionID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    

    
    public static Validator selectElectionsForOwner(int electionOwnerId) {
    	
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.selectElectionsForOwner(electionOwnerId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    

    
    
    public static Validator addElection(ElectionDto electionDto)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.addElection(electionDto, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator addAdditionalUsersToElection(ElectionDto electionDto)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.addAdditionalUsersToElection(electionDto, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator editElection(ElectionDto election)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.editElection(election, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator openElection(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.openElectionAndPopulateCandidates(electionId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator reOpenElection(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.OPEN, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator closeElection(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.CLOSED, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator publishResults(int electionId, String password){
    	Validator val=new Validator();
    	
    	try{
			String sessionID = HeaderService.getUserSessionId();
    		val=Initializer.rmi.publishResults(electionId, password, sessionID);
    	} catch(RemoteException e){
    		e.printStackTrace();
    	}
    	return val;
    }
    
    public static Validator disableElection(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.DELETED, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }


    
    public static Validator selectElectionsForVotingForUser(int userId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.selectElectionsForVoter(userId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }
    
    
    
    public static Validator selectElectionsForResults(int userId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.selectElectionsForResults(userId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }

    
    public static Validator selectResults(int electionId) {
    	Validator validator = new Validator();
    	
    	try {
			String sessionID = HeaderService.getUserSessionId();
			validator  = Initializer.rmi.selectResults(electionId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }
}
