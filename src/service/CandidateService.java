package service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dto.*;
import enumeration.Status;
import rmi.Initializer;

public class CandidateService {

	public static CandidateDto getCandidate(int id){
		CandidateDto candidateDto = new CandidateDto();
		try {
			Validator validator = Initializer.rmi.selectCandidate(id);
			candidateDto = (CandidateDto) validator.getObject();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return candidateDto;
	}
	
    public static ArrayList<CandidateDto> getCandidatesForElection(int election_id){
    	ArrayList<CandidateDto> candidates = new ArrayList<CandidateDto>();
    	try {
    		Validator validator = Initializer.rmi.selectCandidatesOfElection(election_id);
    		candidates =  (ArrayList<CandidateDto>)validator.getObject();	
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return candidates;
    }
    
    public static ArrayList<CandidateDto> getCandidatesForElection(int election_id, Status candidateStatus){
    	ArrayList<CandidateDto> candidates = new ArrayList<CandidateDto>();
    	try {
    		Validator validator = Initializer.rmi.selectCandidatesOfElection(election_id, candidateStatus);
    		candidates =  (ArrayList<CandidateDto>)validator.getObject();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return candidates;
    }
}
