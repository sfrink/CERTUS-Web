package servlets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dto.UserDto;
import dto.Validator;
import service.HtmlService;
import service.NewKeyService;
import service.SignUpService;

/**
 * Servlet implementation class UploadFileServlet
 */
@WebServlet("/signupwithkey")
@MultipartConfig(maxFileSize = 10240)

public class SignUpWithKeyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String firstName = "";
	private String lastName = "";
	private String email = "";
	private String password = "";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpWithKeyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		firstName = request.getParameter("user_firstName");
		lastName = request.getParameter("user_lastName");
		email = request.getParameter("user_email");
		password = request.getParameter("user_password");
		
		InputStream inputStream = null; // input stream of the upload file
        
        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("uploadFile");
        if (filePart != null) {
            // prints out some information for debugging
            int fileSize = (int) filePart.getSize();
            String outUpload = "";
            
            if (fileSize == 0){
            	outUpload = drawUploadingPage();
            }else if (fileSize >= 10240){
            	outUpload = drawUploadingPage();
            }else{
            	//obtains input stream of the upload file
            	inputStream = filePart.getInputStream();
            
            	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    			int nRead;
    			byte[] data = new byte[16384];

    			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
    			  buffer.write(data, 0, nRead);
    			}

    			buffer.flush();
    			
    			byte[] publicKeyBytes = buffer.toByteArray();
            	
            	UserDto newUser = new UserDto();
            	
            	newUser.setFirstName(firstName);
            	newUser.setLastName(lastName);
            	newUser.setEmail(email);
            	newUser.setPassword(password);
            	newUser.setPublicKeyBytes(publicKeyBytes);
            	
            	Validator res = SignUpService.addUserWithPublicKey(newUser);
          
            	if (res.isVerified()){
                	outUpload = drawSuccessfullUpload();
                }else{
                	outUpload = drawFailedUpload(res.getStatus());
                }	
            }
            
			request.setAttribute("mode", "1");
			request.setAttribute("message_alert", "");
			request.setAttribute("message_label", "");
			request.setAttribute("out_modal", "");
			request.setAttribute("out_file", outUpload);
			
            getServletContext().getRequestDispatcher("/signUp.jsp").forward(request, response);
        }
	}
	
	public String drawSuccessfullUpload(){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>Welcome " + firstName + " to Certus Voting System!</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<a class=\"button radius\" href=\"login\">Let us get started!</a>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		
		return out;
	}
	
	
	public String drawFailedUpload(String status){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>OOPS!</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>We couldn't create a new account for you, and that's because: "+ status +"</h3>";
		out += "<h3>Do you want to try again?</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<a href=\"signup\" class=\"button radius\">Yes, let's try again</a>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<a href=\"login\" class=\"button radius\">No, just forget about it.</a>";
		out += "</div>";
		out += "</div>";
		
		return out;	
	
	}
	
	public String drawUploadingPage(){
		String out = "";
		out += "<h5><font color=\"red\">You didn't select a file!</font></h5>";
		out += "<h5>";
		out += "Your file size cannot be larger than 10 bytes.";
		out += "</h5>";
		out += "<h5>";
		out += "<div class=\"row\">";
		out += "<form action=\"uploadkey\" method=\"post\" enctype=\"multipart/form-data\">";
		out += "Select a file: <input name=\"uploadFile\" type=\"file\">";
		out += "<input type=\"submit\" value=\"Upload\" class=\"button radius\" name=\"button_start_uploading\">";
		out += "</form>";
		out += "</div>";

		return out;
	}


}
