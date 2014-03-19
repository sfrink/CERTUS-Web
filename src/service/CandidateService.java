package service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dto.*;
import enumeration.CandidateStatus;
import rmi.Initializer;

public class CandidateService {

	public static CandidateDto getCandidate(int id){
		CandidateDto candidateDto = new CandidateDto();
		try {
			candidateDto = Initializer.rmi.getCandidate(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return candidateDto;
	}
	
    public static ArrayList<CandidateDto> getCandidatesForElection(int election_id){
    	ArrayList<CandidateDto> candidates = new ArrayList<CandidateDto>();
    	try {
			candidates = Initializer.rmi.getCandidatesOfElection(election_id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return candidates;
    }
    
    public static ArrayList<CandidateDto> getCandidatesForElection(int election_id, CandidateStatus candidateStatus){
    	ArrayList<CandidateDto> candidates = new ArrayList<CandidateDto>();
    	try {
			candidates = Initializer.rmi.getCandidatesOfElection(election_id, candidateStatus);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	return candidates;
    }
}
