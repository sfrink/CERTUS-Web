<%@page import="service.HeaderService"%>
<%
if(!HeaderService.isAuthenticated() ||
   !HeaderService.hasAccess("upload")) {
	RequestDispatcher rd = getServletContext().getRequestDispatcher("/login");
	rd.forward(request, response);
}

// get all data from previous screen
String mode = (String) request.getAttribute("mode");
String messageAlert = (String) request.getAttribute("message_alert");
String messageLabel = (String) request.getAttribute("message_label");
String outModal = (String) request.getAttribute("out_modal");
String outFile = (String) request.getAttribute("out_file");
String status = (String) request.getAttribute("uploadStatus");
String outUpload = (String) request.getAttribute("out_upload");
%>

<jsp:include page="headerDefault.jsp" />
<jsp:include page="headerTopBarAuthenticated.jsp" />

<script>
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
   
<!-- display a message -->
<%=messageAlert %> 

	
		<div class="row">
		
		<h3>Upload Public Key</h3>
			<%=outModal %>
		</div>


<jsp:include page="footer.jsp" />  