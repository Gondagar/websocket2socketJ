var ws;
function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
}

function connect() {
	ws = new WebSocket('wss://n2.inline.com.de:8443/chat');
	ws.onmessage = function(data) {
		helloWorld(data.data);
	}
	setConnected(true);
}

function disconnect() {
	if (ws != null) {
		ws.close();
	}
	setConnected(false);
	console.log("Websocket is in disconnected state");
}

function sendData() {
	var data = JSON.stringify({
		'name' : $("#name").val(),
		'message' : $("#message").val()
	})
	ws.send(data);
}

function helloWorld(message) {
	var parse = JSON.parse(message);
	$("#helloworldmessage").append("<tr><br>"  + parse.name  + "<br> : "  + parse.message+  " </td></tr>");
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendData();
	});
});
