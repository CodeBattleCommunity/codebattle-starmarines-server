function validateData() {
	var array = $('input');
	var flag = true;
	clearData();
	for (var i = 0; i < array.length; i++) {
		if(array[i].value == "") {
			$('input[id|=' + array[i].id + ']').after("<span class=\"errors\"> Field should be filled.</span>");
			flag = false;
		}
	}
	return flag;
}