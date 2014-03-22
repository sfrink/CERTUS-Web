package service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dto.*;
import enumeration.Status;
import rmi.Initializer;

public class CandidateService {

	public static Validator selectCandidate(int id){
		Validator validator = null;
		try {
			validator = Initializer.rmi.selectCandidate(id);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return validator;
	}
	
    public static Validator selectCandidatesForElection(int election_id){
    	Validator validator = null;
    	try {
    		validator = Initializer.rmi.selectCandidatesOfElection(election_id);
    		
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return validator;
    }
    
    public static Validator selectCandidatesForElection(int election_id, Status candidateStatus){
    	Validator validator = null;
    	try {
    		validator = Initializer.rmi.selectCandidatesOfElection(election_id, candidateStatus);
    		
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return validator;
    }
    
    public static Validator addCandidatesNames(ArrayList<String> names, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.addCandidatesNamesToElection(names, electionId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator addCandidates(ArrayList<CandidateDto> candidateList, int electionId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.addCandidatesToElection(candidateList, electionId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator editCandidate(CandidateDto candidate)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editCandidate(candidate);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator enableCandidate(int candidateId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editCandidateStatus(candidateId, Status.ENABLED);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
    
    public static Validator disableCandidate(int candidateId)
    {
    	Validator validator = new Validator();
    	
		try {
			validator  = Initializer.rmi.editCandidateStatus(candidateId, Status.DISABLED);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
    }
}
