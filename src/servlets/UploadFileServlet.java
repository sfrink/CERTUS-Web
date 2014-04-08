package servlets;

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

import dto.Validator;
import service.HtmlService;
import service.NewKeyService;

/**
 * Servlet implementation class UploadFileServlet
 */
@WebServlet("/uploadkey")
@MultipartConfig(maxFileSize = 10240)

public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFileServlet() {
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
            
            	Validator res = NewKeyService.uploadPubKey(inputStream);
            	
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
			
            getServletContext().getRequestDispatcher("/newKey.jsp").forward(request, response);
        }
	}
	
	public String drawSuccessfullUpload(){
		String out = "<div>";
		
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<h3>Your new public key was uploaded successfully.</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<div class=\"large-6 medium-6 columns\">";
		out += "<a class=\"button radius\" href=\"newkey\">Finish</a>";
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
		out += "<h3>We couldn't your public key, and that's because: "+ status +"</h3>";
		out += "<h3>Do you want to try again?</h3>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"row\">";
		out += "<form action=\"newkey\" method=\"post\">	";
		out += "<button class=\"button radius\" type=\"submit\" name=\"start_fresh\" value=\"new\">Yes, let's try again</button>		";
		out += "</form>";
		out += "<a href=\"login\">No, just forget about it.</a>";
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
