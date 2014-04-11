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

	
	public Initializer() {

		loadProperties();
		connectRmiServer();
		
	}
	
	
	public void connectRmiServer () {
		String basePath = this.prop.getProperty("rmi_basepath");
		System.getProperties().setProperty("javax.net.ssl.trustStore", basePath + this.prop.getProperty("rmi_file_certificate"));
		System.getProperties().setProperty("javax.net.ssl.trustStorePassword", this.prop.getProperty("rmi_file_certificate_password"));
				
		try {
			System.getSecurityManager();
			String host = this.prop.getProperty("rmi_host");
			int PORT = Integer.parseInt(this.prop.getProperty("rmi_port"));

			Registry registry = LocateRegistry.getRegistry(
			InetAddress.getByName(host).getHostName(), PORT,
			new RMISSLClientSocketFactory());

			/* 
			 * "serverInterface" is the identifier that we'll use to refer
			 * to the remote object that implements the "serverInterface" interface
			 */
			ServerInterface serverInterface = (ServerInterface) registry.lookup(this.prop.getProperty("rmi_registry"));

			System.out.println("RMI connection established");
			this.rmi = serverInterface;
		} catch (Exception e) {
			this.rmi = null;
			System.out.println("RMI connection exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void dummy() {
		// just to get rid of the bug
	}
	
	public void loadProperties() {
		Properties prop = new Properties();
		InputStream input = Initializer.class.getClassLoader().getResourceAsStream("config.properties");;
		
		try {
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			System.out.println(prop.toString());
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		this.prop = prop;
	}
}
