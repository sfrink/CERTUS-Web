package service;

import java.rmi.RemoteException;

import rmi.Initializer;
import dto.Validator;


/**
 * @date : Mar 26, 2014
 * @author : Hirosh Wickramasuriya
 */

public class TallyingService
{

	public static Validator tally(int electionId) {
		Validator validator = new Validator();

		try {
			validator = Initializer.rmi.voteProgressStatusForElection(electionId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}

	public static Validator voteProgressStatusForElection(int electionId){
		Validator validator = new Validator();

		try {
			validator = Initializer.rmi.voteProgressStatusForElection(electionId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}
}
