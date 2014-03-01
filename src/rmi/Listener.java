package rmi;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;


public class Listener implements javax.servlet.ServletContextListener {

	private Properties prop;
	
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// triggered on app start
		Initializer i = new Initializer();
	}
	
	
	
	
	
	
}
