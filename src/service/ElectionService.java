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
}
