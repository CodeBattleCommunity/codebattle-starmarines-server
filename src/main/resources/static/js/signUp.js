function validateData() {
	var array = $('input[type|="text"]');
	var flag = true;
	clearData();
	for (var i = 0; i < array.length; i++) {
		if(array[i].value == "") {
			$('input[id|=' + array[i].id + ']').after("<span class=\"errors\"> Field should be filled.</span>");
			flag = false;
		}
	}
	var passwords = $('input[type|="password"]');
	if (passwords[0].value.length < 5) {
		$('input[id|=' + passwords[0].id + ']').after("<span class=\"errors\"> Password should has at least 5 letters.</span>");
		flag = false;
	} else {
		if (passwords[0].value != passwords[1].value) {
			$('input[id|=' + passwords[0].id + ']').after("<span class=\"errors\"> Password does not match with re-password.</span>");
			flag = false;
		}
	}
	if (passwords[1].value.length < 5) {
		$('input[id|=' + passwords[1].id + ']').after("<span class=\"errors\"> Repassword should has at least 5 letters.</span>");
		flag = false;
	} else {
		if (passwords[1].value != passwords[0].value) {
			$('input[id|=' + passwords[1].id + ']').after("<span class=\"errors\"> Repassword does not match with password.</span>");
			flag = false;
		}
	}
	return flag;
}