angular.module("DetetiveApp.websocketService").service("WebsocketService", function($q, $timeout) {

    var messageListener = $q.defer(),
        commandListener = $q.defer(),
        userinfoListener = $q.defer(),
        gameinfoListener = $q.defer();

    var service = {}, socket = {
      client: null,
      stomp: null
    }, messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/websocket-poc";

    // TOPICS
    service.CHAT_TOPIC = "/topic/chat";
    service.INFO_TOPIC = "/topic/info";

    // QUEUES
    service.COMMAND_QUEUE = '/user/queue/command';
    service.INFO_QUEUE = '/user/queue/info';

    // BROKERS
    service.CHAT_BROKER = "/app/message";
    service.COMMAND_BROKER = "/app/command";
    service.INFO_BROKER = "/app/info";

    service.receive_message = function() {
      return messageListener.promise;
    };

    service.receive_command = function() {
      return commandListener.promise;
    };

    service.receive_user_info = function() {
      return userinfoListener.promise;
    };

    service.receive_game_info = function() {
      return gameinfoListener.promise;
    };

    service.send_message = function(message) {
      socket.stomp.send(service.CHAT_BROKER, {},
          JSON.stringify({
            text: message,
            recipient: ''
          }));
    };

    service.send_command = function(command, callback) {
      socket.stomp.send(service.COMMAND_BROKER, {}, command);
      if (callback)
        callback();
    };

    service.send_info_request = function(info_request, callback) {
      socket.stomp.send(service.INFO_BROKER, {}, info_request);
      if (callback)
        callback();
    };

    var reconnect = function() {
      $timeout(function() {
        initialize();
      }, this.RECONNECT_TIMEOUT);
    };

    var startListener = function() {
        socket.stomp.subscribe(service.CHAT_TOPIC, function(data) {
            messageListener.notify(JSON.parse(data.body));
        });

        socket.stomp.subscribe(service.COMMAND_QUEUE, function (data) {
            commandListener.notify([service, JSON.parse(data.body)]);
        });

        socket.stomp.subscribe(service.INFO_QUEUE, function (data) {
            userinfoListener.notify(JSON.parse(data.body));
        });

        socket.stomp.subscribe(service.INFO_TOPIC, function (data) {
            gameinfoListener.notify(JSON.parse(data.body));
        });

        var request = JSON.stringify({type: "PERSONAGENS_DISPONIVEIS"});
        socket.stomp.send(service.INFO_BROKER, {}, request);
    };

    var initialize = function() {
      socket.client = new SockJS(service.SOCKET_URL);
      socket.stomp = Stomp.over(socket.client);
      socket.stomp.connect({}, startListener);
      socket.stomp.onclose = reconnect;
    };

    initialize();
    return service;
  });