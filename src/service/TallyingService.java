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
			validator = Initializer.getRmi().selectResults(electionId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
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
			validator = Initializer.getRmi().voteProgressStatusForElection(electionId, sessionID);
		} catch (Exception e) {
			HeaderService.errorLogout(request);
		}
		return validator;
	}
}
