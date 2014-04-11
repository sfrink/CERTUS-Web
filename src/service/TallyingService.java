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
		
	public static Validator results(int electionId) {
		Validator validator = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId();
			validator = Initializer.rmi.selectResults(electionId, sessionID);
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
			String sessionID = HeaderService.getUserSessionId();
			validator = Initializer.rmi.voteProgressStatusForElection(electionId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}
}
