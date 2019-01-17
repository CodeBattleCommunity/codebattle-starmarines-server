$.get( "./settings.html?type=TURN_DURATION", function( data ) {
	alert(data);
});




function CFG() {
	//server - mapId, turnDelay, planet types(with stats), players
    this.gameId = $("#gameId").text();
	this.turnDelay = $.get( "./settings.html?type=TURN_DURATION", function( data ) {
		return JSON.parse(data).TurnDelay;
	});
}

function Map(data) {
//gradient, expansion color, planets
}

Map.prototype = new THREE.Scene();

function Planet(d) {
	this._id = d.id;//: 1
	this._name = d.name;//: "Zeus"
	this.neighbors = d.neighbors;//: [2, 3, 4]
	this.owner;//: "neutral"
	this.regenRate;//: 0.3
	this.type;//: "TYPE_D"
	this.unitsCount;//: 0
	this.xCoord;//: 0
	this.yCoord//: 0
	this.actions;// {unitCount:6, from:10, to:3, owner:???}
	this.prevStateOwner;
}

Planet.updateStats = function(data) {
	//update Actions, then update state,
}

function Action(from, to, count, owner) {

}

var GAME = {
	started: false,
	gameId: $('#gameId').text(),
	turn : 0
}




Planet.prototype = new THREE.Object3D();

