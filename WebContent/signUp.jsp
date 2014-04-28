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
%>


<script>

function removeElement(element){
	var elem = document.getElementById(element);
	elem.parentNode.removeChild(elem);
}

function removeUnchecked(){
	
	var basic = document.getElementById("rdn_basic_signup").checked;
	var upload = document.getElementById("rdn_upload_key").checked;
	
	if (basic || upload){
		removeElement("new_key_password");
		removeElement("new_key_password_confirm");
	}
	
}

function showAdvanced(){
	document.getElementById("show_advanced_options").style.display = "none";
	document.getElementById("hide_advanced_options").style.display = "block";
	
	document.getElementById("div_generate_key").style.display = "block";
	document.getElementById("div_upload_key").style.display = "block";
	
	onBasicSignupRadioClick();
}

function hideAdvanced(){
	document.getElementById("show_advanced_options").style.display = "block";
	document.getElementById("hide_advanced_options").style.display = "none";
	
	document.getElementById("div_generate_key").style.display = "none";
	document.getElementById("div_upload_key").style.display = "none";
	onBasicSignupRadioClick();
}

function onBasicSignupRadioClick(){
	document.getElementById("rdn_basic_signup").checked=true;
	document.getElementById("rdn_generate_new_keys").checked=false;
	document.getElementById("new_key_password").disabled=true;
	document.getElementById("new_key_password_confirm").disabled=true;
	
	document.getElementById("rdn_upload_key").checked=false;
	document.getElementById("FileInput").disabled=true;
	
	document.getElementById("apiFileError").style.display = "none";
    document.getElementById("emptyFileError").style.display = "none";
    document.getElementById("largeFileError").style.display = "none";
}


function onGenerateKeysRadioClick(){
	document.getElementById("rdn_basic_signup").checked=false;
	document.getElementById("new_key_password").disabled=false;
	document.getElementById("new_key_password_confirm").disabled=false;
	
	document.getElementById("rdn_upload_key").checked=false;
	document.getElementById("FileInput").disabled=true;
	
	document.getElementById("apiFileError").style.display = "none";
    document.getElementById("emptyFileError").style.display = "none";
    document.getElementById("largeFileError").style.display = "none";
}

function onUploadRadioClick(){
	document.getElementById("rdn_basic_signup").checked=false;
	document.getElementById("rdn_generate_new_keys").checked=false;
	document.getElementById("new_key_password").disabled=true;
	document.getElementById("new_key_password_confirm").disabled=true;
	
	document.getElementById("FileInput").disabled=false;
	
	document.getElementById("apiFileError").style.display = "none";
    document.getElementById("emptyFileError").style.display = "none";
    document.getElementById("largeFileError").style.display = "none";	
}

function prepareForm() {
	isPublicKeySelected = document.getElementById("rdn_upload_key").checked;
	
	if (!isPublicKeySelected){
		removeUnchecked(); 
		return true;
	}
	
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
    
    removeUnchecked();    
    return true;
    
}

</script>


<jsp:include page="headerDefault.jsp" /> 
<jsp:include page="headerTopBarDefault.jsp" />

<%=messageAlert %>
  
  <div class="row">
		<%=outModal %>
  </div>





<jsp:include page="footer.jsp" />