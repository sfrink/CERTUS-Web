$(document).ready(function() {
    $('#button_vote_back').click(function() {
    	var url = 'main';
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
    	var url = 'main';
    	var form = $('<form action="' + url + '" method="post"></form>');
    	$('body').append(form);
    	$(form).submit();
    });
});

$(document).ready(function() {
	$("#close-reveal-modal-modified").click(function () {
	    var message = "Warning! You are about to lose unsaved changes.\n\nClick 'OK' to discard any unsaved changes.\n\nClick 'CANCEL' to return editing.\n\n";
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
		$('#new_election_users_invite').hide();		
		
		if(optionId == 2) {
			$('#new_election_users_column').show();
			$('#new_election_users_invite').show();
			$('#new_election_users').prop('required',true);
		} else if(optionId == 1) {
			$('#new_election_users_column').hide();
			$('#new_election_users_invite').hide();
			$('#new_election_users').prop('required',false);
		}
	});
});

$(document).ready(function() {
	$('#edit_election_availability').on('change', function() {
		var optionId = $(this).val();
		$('#edit_election_users_column').hide();
		$('#edit_election_users_invite').hide();		
		
		if(optionId == 2) {
			$('#edit_election_users_column').show();
			$('#edit_election_users_invite').show();
			$('#edit_election_users').prop('required',true);
		} else if(optionId == 1) {
			$('#edit_election_users_column').hide();
			$('#edit_election_users_invite').hide();
			$('#edit_election_users').prop('required',false);
		}
	});
});

$(document).ready(function() {
	$("[name='new_election_users_invited']").click(function () {
		if($("[name=new_election_users_invited]:checked").length > 0) {
			$('#new_election_users').prop('required',false);
		} else {
			$('#new_election_users').prop('required',true);
		}
	});
});

$(document).ready(function() {
	$("[name='edit_election_users_invited']").click(function () {
		if($("[name=edit_election_users_invited]:checked").length > 0) {
			$('#edit_election_users').prop('required',false);
		} else {
			$('#edit_election_users').prop('required',true);
		}
	});
});