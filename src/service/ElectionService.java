package service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.Initializer;
import dto.ElectionDto;
import dto.Validator;
import enumeration.ElectionStatus;

public class ElectionService {
	
	public static Validator selectElection(int id) {
		Validator validator = null;
		try {
			validator = Initializer.rmi.selectElection(id);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}
	
    public static Validator selectElections(ElectionStatus electionStatus) {
    	Validator validator = null;
    	
		try {
			validator  = Initializer.rmi.selectElections(electionStatus);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator selectElections() {
    	Validator validator = null;
		try {
			validator = Initializer.rmi.selectElections();
			 
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }

    public static Validator selectElectionsOwnedByUser(int electionOwnerId, ElectionStatus electionStatus) {
    	Validator validator = null;
    	
		try {
			validator  = Initializer.rmi.selectElectionsOwnedByUser(electionOwnerId, electionStatus);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator selectElectionsOwnedByUser(int electionOwnerId) {
    	
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.selectElectionsOwnedByUser(electionOwnerId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator addElectionWithCandidates(ElectionDto electionDto)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.addElectiodWithCandidates(electionDto);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator editElection(ElectionDto election)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editElection(election);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator openElection(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.OPEN);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator closeElection(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.CLOSED);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator disableElection(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.DELETED);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator publishElectionResults(int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editElectionStatus(electionId, ElectionStatus.PUBLISHED);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
}
