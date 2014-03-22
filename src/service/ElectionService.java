package service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.Initializer;
import dto.ElectionDto;
import dto.Validator;
import enumeration.ElectionStatus;

public class ElectionService {
	
	public static ElectionDto getElection(int id) {
		ElectionDto electionDto = new ElectionDto();
		try {
			Validator validator = Initializer.rmi.selectElection(id);
			electionDto = (ElectionDto)validator.getObject();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return electionDto;
	}
	
    public static ArrayList<ElectionDto> selectElections(ElectionStatus electionStatus) {
    	
    	ArrayList<ElectionDto> elections = new ArrayList<ElectionDto>();
		try {
			Validator validator  = Initializer.rmi.selectElections(electionStatus);
			elections = (ArrayList<ElectionDto>)validator.getObject();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return elections;
    }
    
    public static ArrayList<ElectionDto> selectElections() {
    	ArrayList<ElectionDto> elections = new ArrayList<ElectionDto>();
		try {
			Validator validator = Initializer.rmi.selectElections();
			elections = (ArrayList<ElectionDto>)validator.getObject();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return elections;
    }

}
