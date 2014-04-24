package service;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

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
		
	public static Validator results(HttpServletRequest request, int electionId) {
		Validator validator = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId(request);
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
	public static Validator voteProgressStatusForElection(HttpServletRequest request, int electionId){
		Validator validator = new Validator();

		try {
			String sessionID = HeaderService.getUserSessionId(request);
			validator = Initializer.rmi.voteProgressStatusForElection(electionId, sessionID);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return validator;
	}
}
