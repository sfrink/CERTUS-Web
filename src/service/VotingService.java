package service;

import java.rmi.RemoteException;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;

import rmi.Initializer;
import dto.Validator;
import dto.VoteDto;

public class VotingService {

	
	public static Validator saveVote(HttpServletRequest request, VoteDto vote) {
		Validator v1 = new Validator();
		Validator v2 = new Validator();
		
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			v2 = Initializer.rmi.vote(vote, sessionID);

			if(v2.isVerified()) {
				v1.setVerified(true);
				v1.setStatus(v2.getStatus());
			} else {
				v1.setVerified(false);
				v1.setStatus(v2.getStatus());
				v1.setObject(v2.getObject());
			}
		} catch (Exception e) {
			e.printStackTrace();
			v1.setVerified(false);
			v1.setStatus(e.toString() + "RMI failed");
		}
				
		return v1;
	}

	
	
	public static Validator encryptCandidateId(HttpServletRequest request, int candidateId, int electionId) {
		Validator v1 = new Validator();
		Validator v2 = new Validator();

		// get public key from the server
		try {
			String sessionID = HeaderService.getUserSessionId(request);
			v2 = Initializer.rmi.getTallierPublicKey(electionId, sessionID);
		
			if(v2.isVerified()) {
				String cipherText = "";
				// get public key
				PublicKey pk = (PublicKey) v2.getObject();

				// prepare plaintext
				String plaintext = Integer.toString(candidateId);
				byte[] plainBytes = plaintext.getBytes();

				
				// encrypt candidate id
				Cipher enc = Cipher.getInstance("RSA");
				
				enc.init(Cipher.ENCRYPT_MODE, pk);
				byte[] cipherBytes= enc.doFinal(plainBytes);
				cipherText = byteArraytoHex(cipherBytes);

				v1.setVerified(true);
				v1.setObject(cipherText);			
			} else {
				v1.setVerified(false);
				v1.setStatus(v2.getStatus());
			}
		} catch(RemoteException e) {
			v1.setVerified(false);
			v1.setStatus("RMI call failed");
		} catch(Exception e) {
			v1.setVerified(false);
			v1.setStatus("Encryption failed");			
		}
		
		return v1;		
	}
	
	

	public static String byteArraytoHex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(Integer.toString((arr[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}	
}
