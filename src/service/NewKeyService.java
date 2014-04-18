package service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;






import rmi.Initializer;
import sun.misc.IOUtils;
import dto.Validator;

public class NewKeyService {

	
	public static Validator generateNewKeys(String newKeyPass, String userPassword){
		Validator res = new Validator();
		int userID = HeaderService.getUserId();
		String sessionID = HeaderService.getUserSessionId();
		try {
			res = Initializer.rmi.generateNewKeys(userID, newKeyPass, userPassword, sessionID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		return res;
	}
	
	public static Validator uploadPubKey(InputStream dataStream, String userPassword) throws IOException{
		
		Validator res = new Validator();
		String sessionID = HeaderService.getUserSessionId();
		
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = dataStream.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}

			buffer.flush();
			
			res = Initializer.rmi.uploadPubKey(buffer.toByteArray(), userPassword, sessionID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			res.setVerified(false);
			res.setStatus("RMI failure.");
		}
		return res;

	}
	
	
}
