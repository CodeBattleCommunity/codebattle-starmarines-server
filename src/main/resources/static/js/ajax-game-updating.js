function getMap(mapId){
	var xmlhttp;
	 if (window.XMLHttpRequest)
	   {
	   xmlhttp=new XMLHttpRequest();
	   }
	 else
	   {
	   xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	   }
	 var request = "game/viewData?gameid=" + mapId + "&type=GAME_FIELD";
	 xmlhttp.open("GET", request, true);
	 xmlhttp.send();
	 xmlhttp.onreadystatechange=function()
	   {
	   if (xmlhttp.readyState==4 && xmlhttp.status==200)
	     {
		   document.getElementById("stdout").innerHTML = xmlhttp.responseText;
	     }
	   };
}

function getChanges(mapId){
	var xmlhttp;
	 if (window.XMLHttpRequest)
	   {
	   xmlhttp=new XMLHttpRequest();
	   }
	 else
	   {
	   xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	   }
	 var request = "game/viewData?gameid=" + mapId + "&type=PLAYERS_ACTIONS";
	 xmlhttp.open("GET", request, true);
	 xmlhttp.send();
	 xmlhttp.onreadystatechange=function()
	   {
	   if (xmlhttp.readyState==4 && xmlhttp.status==200)
	     {
		   document.getElementById("stdout").innerHTML = xmlhttp.responseText;
	     }
	   };
}

var gameTableUpdater = 0;

var updateGameTable = function() {
    $.ajax({
        url: "gameTable.html",
        type: "get",
        data: {
            gameId: gameId
        }
    }).success(function( data ) {
        jQuery("#beforeStartTableContainer").html(data) ;
    });
}

function runTableUpdater() {
    if(!gameTableUpdater) {
        updateGameTable();
        gameTableUpdater = setInterval(updateGameTable, 4000);
    }
}

function stopTableUpdater() {
    if(gameTableUpdater) {
        clearInterval(gameTableUpdater);
    }
    jQuery("#beforeStartTableContainer").html("");
}

var gameListener;

function checkGame() {
    $.ajax({
        url: "checkGame.html",
        type: "get",
        data: {
            gameId: gameId
        }
    }).success(function (data) {
        try {
            if (data.length > 20) {
                //ахаха что ты делаешь
                window.location.href = "/battle.html";
            }
        } catch(a) {
            // работает - не трогай
        }
    });
}

function startGameListener() {
    if(!gameListener) {
        checkGame();
        gameListener = setInterval(checkGame, 6000);
    }
}