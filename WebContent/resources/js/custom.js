$(document).on('close', '[data-reveal]', function () {
  var modal = $(this);

  
  
  modal.foundation('reveal', 'open');
  alert("A");
  
});