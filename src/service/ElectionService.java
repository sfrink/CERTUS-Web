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

	static String sessionID = ""; //to be changed with the real session ID
	
	public static Validator selectAllElectionsForVoter(int userId) {
		Validator v1 = new Validator();

		try {
			v1 = Initializer.rmi.selectAllElectionsForVoter(userId, sessionID);
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


	public static Validator selectElection(int id) {
		Validator v1 = new Validator(), v2 = new Validator(), v3 = new Validator();
		try {
			v1 = Initializer.rmi.selectElection(id, sessionID);
			
			if(v1.isVerified()) {
				ElectionDto e = (ElectionDto) v1.getObject();
				
				v2 = Initializer.rmi.selectCandidatesOfElection(e.getElectionId(), Status.ENABLED, sessionID);
				
				if(v2.isVerified()) {
					ArrayList<CandidateDto> candidates = (ArrayList<CandidateDto>) v2.getObject();
					e.setCandidateList(candidates);					

					v3.setVerified(true);
					v3.setObject(e);
				} else {
					v3.setVerified(false);
					v3.setStatus(v2.getStatus());
				}
			} else {
				v3.setVerified(false);
				v3.setStatus(v1.getStatus());
			}
		} catch (RemoteException e) {
			v3.setVerified(false);
			v3.setStatus("RMI call failed");
		}
		
		return v3;
	}
	
	public static Validator selectElectionFullDetail(int electionId) {
		Validator validator = null;
    	
		try {
			validator  = Initializer.rmi.selectElectionFullDetail(electionId, sessionID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}
	
    public static Validator selectElections(ElectionStatus electionStatus) {
    	Validator validator = null;
    	
		try {
			validator  = Initializer.rmi.selectElections(electionStatus, sessionID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator selectElectionsNotDeleted() {
    	Validator validator = null;
    	
		try {
			validator  = Initializer.rmi.selectElectionsNotInStatus(ElectionStatus.DELETED, sessionID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator selectElections() {
    	Validator validator = null;
		try {
			validator = Initializer.rmi.selectElections(sessionID);
			 
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }

    public static Validator selectElectionsOwnedByUser(int electionOwnerId, ElectionStatus electionStatus) {
    	Validator validator = null;
    	
		try {
			validator  = Initializer.rmi.selectElectionsOwnedByUser(electionOwnerId, electionStatus, sessionID);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator selectElectionsOwnedByUser(int electionOwnerId) {
    	
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.selectElectionsOwnedByUser(electionOwnerId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator addElection(ElectionDto electionDto)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.addElection(electionDto, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator editElection(ElectionDto election)
    {
    	Validator validator = new Validator();
    	
		try {
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
			//validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.OPEN);
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
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.CLOSED, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator publishResults(int electionId){
    	Validator val=new Validator();
    	
    	try{
    		val=Initializer.rmi.publishResults(electionId, sessionID);
    	} catch(RemoteException e){
    		e.printStackTrace();
    	}
    	return val;
    }
    
    public static Validator disableElection(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.DELETED, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }


    
    public static Validator selectElectionsForVotingForUser(int userId) {
    	Validator validator = new Validator();
    	
    	try {
			validator  = Initializer.rmi.selectAllElectionsForVoter(userId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }
    
    public static Validator selectElectionsForResultsForUser(int userId) {
    	Validator validator = new Validator();
    	
    	try {
			validator  = Initializer.rmi.selectElections(ElectionStatus.PUBLISHED, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }

    public static Validator selectResults(int electionId) {
    	Validator validator = new Validator();
    	
    	try {
			validator  = Initializer.rmi.selectResults(electionId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
		return validator;  
    }

}
