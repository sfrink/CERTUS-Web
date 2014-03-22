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
}
