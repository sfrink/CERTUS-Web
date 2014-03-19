package service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.Initializer;
import dto.ElectionDto;
import enumeration.ElectionStatus;

public class ElectionService {
	
	public static ElectionDto getElection(int id) {
		ElectionDto electionDto = new ElectionDto();
		try {
			electionDto = Initializer.rmi.getElection(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return electionDto;
	}
	
    public static ArrayList<ElectionDto> getElections(ElectionStatus electionStatus) {
    	
    	ArrayList<ElectionDto> elections = new ArrayList<ElectionDto>();
		try {
			elections = Initializer.rmi.getElections(electionStatus);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return elections;
    }
    
    public static ArrayList<ElectionDto> getElections() {
    	ArrayList<ElectionDto> elections = new ArrayList<ElectionDto>();
		try {
			elections = Initializer.rmi.getElections();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return elections;
    }

}
