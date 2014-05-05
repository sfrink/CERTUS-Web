package rmi;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;


public class Initializer {
	
	public static Properties prop;
	public static ServerInterface rmi;

	public static ServerInterface getRmi() {
		if(rmi == null) {
			connectRmiServer();
		}
		
		return rmi;
	}
		
	public static void connectRmiServer () {
		String basePath = prop.getProperty("rmi_basepath");
		System.getProperties().setProperty("javax.net.ssl.trustStore", basePath + prop.getProperty("rmi_file_certificate"));
		System.getProperties().setProperty("javax.net.ssl.trustStorePassword", prop.getProperty("rmi_file_certificate_password"));
				
		try {
			System.getSecurityManager();
			String host = prop.getProperty("rmi_host");
			int PORT = Integer.parseInt(prop.getProperty("rmi_port"));

			Registry registry = LocateRegistry.getRegistry(
			InetAddress.getByName(host).getHostName(), PORT,
			new RMISSLClientSocketFactory());

			/* 
			 * "serverInterface" is the identifier that we'll use to refer
			 * to the remote object that implements the "serverInterface" interface
			 */
			rmi = (ServerInterface) registry.lookup(prop.getProperty("rmi_registry"));

			System.out.println("RMI connection established");
		} catch (Exception e) {
//			rmi = null;
			System.out.println("RMI connection exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void loadProperties() {
		Properties _prop = new Properties();
		InputStream input = Initializer.class.getClassLoader().getResourceAsStream("config.properties");
		
		try {
			// load a properties file
			_prop.load(input);			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		prop = _prop;
	}
}
