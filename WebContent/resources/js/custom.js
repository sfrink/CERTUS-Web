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
	    var message = "Warning!\n\nYou are about to close this window and all entered information will be lost.\n\nClick 'OK' to close this window and dismiss entered information.\n\nClick 'CANCEL' to stay on this window.\n\n";
        if (confirm(message)) { 
        	$('#modal_window').foundation('reveal', 'close');
        	return true;
        } else {
        	return false;
        }
	});
});

$(document).ready(function() {
	$("#close-reveal-modal-modified_election").click(function () {
	    var message = "Warning!\n\nVOTING is not complete! You are about to close this window and your vote will not be saved.\n\nClick 'OK' to dismiss your vote.\n\nClick 'CANCEL' to stay on this window and complete the voting process\n\n";
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


$(document).ready(function() {
	$('#new_election_availability').on('change', function() {
		var optionId = $(this).val();

		$('#new_election_users_column').hide();
		
		if(optionId == 2) {
			$('#new_election_users_column').show();
			$('#new_election_users').val("");
		} else if(optionId == 1) {
			$('#new_election_users_column').hide();
			$('#new_election_users').val("void");
		}
	});
});