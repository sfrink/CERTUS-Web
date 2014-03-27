var newCandNum = 0;
var editCandNum = 0;

function addNewCandRow(frm) {
	newCandNum ++;
	var candRowId = "rowNewCandId";
	var candRowName = "rowNewCandName";
	var candHolderId = "rowNewCandHolder";
	
	var row = '<div id="' + candRowId + newCandNum + '"><label>Candidate Name</label><input type="text" name="'+ candRowName +'" value=""><a class="button tiny radius alert" href="javascript:void(0)" onclick="removeNewCandRow('+newCandNum+');">Remove</a></div>';
		
	jQuery('#' + candHolderId).append(row);
}

function addEditCandRow(frm) {
	editCandNum ++;
	var candRowId = "rowEditCandId";
	var candRowName = "rowEditCandNameNew";
	var candHolderId = "rowEditCandHolder";
	
	var row = '<div id="' + candRowId + "new" + editCandNum + '"><label>Candidate Name</label><input type="text" name="' + candRowName + '" value=""><a class="button tiny radius alert" href="javascript:void(0)" onclick="removeEditCandRow(\'new' + editCandNum + '\');">Remove</a></div>';
		
	jQuery('#' + candHolderId).append(row);
}

function removeNewCandRow(rnum) {
	var candRowId = "rowNewCandId";

	jQuery('#' + candRowId + rnum).remove();
}

function removeEditCandRow(rnum) {
	var candRowId = "rowEditCandId";

	jQuery('#' + candRowId + rnum).remove();
}

function removeEditExistingCandRow(rnum) {
	var candRowId = "rowEditCandId";
	var statName = "edit_candidate_status" + rnum;
	
	$('[name="' + statName + '"]').val(0);
	jQuery('#' + candRowId + rnum).hide();
}
