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

	/**
	 * @param electionId
	 * @return Validator with ElectionDto
	 */
	public static Validator Results(int electionId) {
		Validator validator = new Validator();

		try {
			validator = Initializer.rmi.selectResults(electionId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}

	
	/**
	 * @param electionId
	 * @return Validator with ElectionProgressDto
	 */
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
