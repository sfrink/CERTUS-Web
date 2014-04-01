$(document).ready(function() {
    $('#button_vote_back').click(function() {
    	var url = 'voting';
    	var elec_id = $(this).attr("value");
    	var form = $('<form action="' + url + '" method="post">' +
    	  '<input type="hidden" name="button_vote" value="' + elec_id + '" />' +
    	  '</form>');
    	$('body').append(form);
    	$(form).submit();
    });
});

$(document).ready(function() {
    $('#button_vote_complete').click(function() {
    	var url = 'voting';
    	var form = $('<form action="' + url + '" method="post"></form>');
    	$('body').append(form);
    	$(form).submit();
    });
});

$(document).ready(function() {
	$("#close-reveal-modal-modified").click(function () {
	    var message = "Warning! Information you have entered will not be saved. Are you sure you want to proceed?";
        if (confirm(message)) { 
        	$('#modal_window').foundation('reveal', 'close');
        	return true;
        } else {
        	return false;
        }
	});
});

$(document).ready(function() {
	$("#close-reveal-modal-modified-force").click(function () {
    	$('#modal_window').foundation('reveal', 'close');
	});
});
