<%@page import="service.HeaderService"%>
<%
	if(HeaderService.isAuthenticated(request)) {
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
		rd.forward(request, response);
	}

	// get all data from previous screen
	String mode = (String) request.getAttribute("mode");
	String messageAlert = (String) request.getAttribute("message_alert");
	String messageLabel = (String) request.getAttribute("message_label");
	String outModal = (String) request.getAttribute("out_modal");
	String outUpload = (String) request.getAttribute("out_file");
	String result = "";
%>


<script>
function onGenerateKeysRadioClick(){
	
	document.getElementById("new_key_password").disabled=false;
	document.getElementById("new_key_password_confirm").disabled=false;
	document.getElementById("btn_generate_new_keys").disabled=false;
	
	document.getElementById("upload_old_key").checked=false;
	
	document.getElementById("FileInput").disabled=true;
	document.getElementById("button_start_uploading").disabled=true;
	
	document.getElementById("apiFileError").style.display = "none";
    document.getElementById("emptyFileError").style.display = "none";
    document.getElementById("largeFileError").style.display = "none";

}

function onUploadRadioClick(){
	
	document.getElementById("new_key_password").disabled=true;
	document.getElementById("new_key_password_confirm").disabled=true;
	document.getElementById("btn_generate_new_keys").disabled=true;
	
	document.getElementById("rdn_generate_new_keys").checked=false;
	
	document.getElementById("FileInput").disabled=false;
	document.getElementById("button_start_uploading").disabled=false;
	
	document.getElementById("apiFileError").style.display = "none";
    document.getElementById("emptyFileError").style.display = "none";
    document.getElementById("largeFileError").style.display = "none";
}

function showFileSize() {
    var input, file;

    document.getElementById("apiFileError").style.display = "none";
    document.getElementById("emptyFileError").style.display = "none";
    document.getElementById("largeFileError").style.display = "none";
    
    // (Can't use `typeof FileReader === "function"` because apparently
    // it comes back as "object" on some browsers. So just see if it's there
    // at all.)
    
    if (!window.FileReader) {
        document.getElementById("apiFileError").style.display = "block";
        return false;
    }

    input = document.getElementById('FileInput');
    if (!input.files) {
    	document.getElementById("apiFileError").style.display = "block";
    	return false;
    }
    else if (!input.files[0]) {
    	document.getElementById("emptyFileError").style.display = "block";
    	return false;
    }
    else {
        file = input.files[0];
        if (file.size > 10240){
        	document.getElementById("largeFileError").style.display = "block";
        	return false;	
        }
    }
    
    return true;
    
}

</script>


<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarDefault.jsp" />

<%
	if(outUpload != null){
		result = outUpload;
	}else{
		result = outModal;
	}
%>

<%=messageAlert %>
  
  <div class="row">
		<%=result %>
  </div>





<jsp:include page="footer.jsp" />