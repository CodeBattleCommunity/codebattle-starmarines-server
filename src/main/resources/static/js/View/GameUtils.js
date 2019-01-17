function notGameStatus(message) {
	if (message.gameState == "finished") {
		$(".gameStatusMessage").css("display","block")
		$("#notStarted").css("display","block")
	} else { //not started
		$(".gameStatusMessage").css("display","block")
		$("#finished").css("display","block")
	}
}


