

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");		
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String basePath = "/Users/dkarmazi/Desktop/files/";
//		System.getProperties().setProperty("java.security.policy", basePath + "policy");
		System.getProperties().setProperty("javax.net.ssl.trustStore", basePath + "ClientTrustedCerts");
		System.getProperties().setProperty("javax.net.ssl.trustStorePassword", "CertusCertPass");
				
		try {
			System.getSecurityManager();
//			if (System.getSecurityManager() == null) {
//				System.setSecurityManager(new RMISecurityManager());
//			}
			
			String host = "127.0.0.1";

			int PORT = 2019;

			String input = request.getParameter("username");
			
			Registry registry = LocateRegistry.getRegistry(
			InetAddress.getByName(host).getHostName(), PORT,
			new RMISSLClientSocketFactory());

		
			// "serverInterface" is the identifier that we'll use to refer
			// to the remote object that implements the "serverInterface"
			// interface
			ServerInterface serverInterface = (ServerInterface) registry.lookup("CertusServer");

			
			String message = "blank";
			
			message = serverInterface.sayHello(input);
			
			request.setAttribute("atr", message);
			RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
			rd.forward(request, response);
			return;
			
			
			
			
			
			
		} catch (Exception e) {
			System.out.println("Certus Client exception: " + e.getMessage());
			e.printStackTrace();
		}

		
		
		
		
		
		
		
		
		
		
		
		
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
		
//		Validator v = LoginService.authenticate(username, password);
//		request.setAttribute("s_status", v.getStatus());
//
//		if(v.isVerified()) {
//			RequestDispatcher rd = request.getRequestDispatcher("editList.jsp");
//			rd.forward(request, response);
//			return;
//		} else {
//			RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
//			rd.forward(request, response);
//			return;
//		}

		
	}

}
