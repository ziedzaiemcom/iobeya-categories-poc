const client = new StompJs.Client({
  let protocol = (window.location.protocol == "https:") ? "wss" : "ws";
  brokerURL: `${protocol}://${window.location.host}/ws`,
  debug: function (str) {
    	//console.log(str);
  },
  reconnectDelay: 1000,
  heartbeatIncoming: 1000,
  heartbeatOutgoing: 1000,
});

// Fallback code
if (typeof WebSocket !== 'function') {
  // For SockJS you need to set a factory that creates a new SockJS instance
  // to be used for each (re)connect
  client.webSocketFactory = function () {
    // Note that the URL is different from the WebSocket URL
    return new SockJS('/ws');
  };
}

client.onConnect = function (frame) {
  // Do something, all subscribes must be done is this callback
  // This is needed because this will be executed after a (re)connect
  
  //console.log('Connected: ' + frame);
  vueApp.ws_state = true;
  client.subscribe('/topic/broadcast', function(messageOutput) {
	//console.log("received " + messageOutput);
	vueApp.events.push(JSON.parse(messageOutput.body));
  });

};

client.onStompError = function (frame) {
  vueApp.ws_state = false;
  // Will be invoked in case of error encountered at Broker
  // Bad login/passcode typically will cause an error
  // Complaint brokers will set `message` header with a brief message. Body may contain details.
  // Compliant brokers will terminate the connection after any error
  console.log('Broker reported error: ' + frame.headers['message']);
  console.log('Additional details: ' + frame.body);
};

client.activate();