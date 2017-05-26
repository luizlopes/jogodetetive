angular.module("DetetiveApp.chatController").controller("ChatCtrl", function($scope, WebsocketService) {
  $scope.messages = [];
  $scope.message = "";
  $scope.max = 140;

  $scope.addMessage = function() {
    WebsocketService.send_message($scope.message);
    $scope.message = "";
  };

  WebsocketService.receive_message().then(null, null, function(message) {
    $scope.messages.push(message);
  });

  /*WebsocketService.receive_players().then(null, null, function(players) {
    for (var i = 0, len = players.length; i < len; i++) {
      var message = {
              text: players[i].usuario + " é o personagem " + players[i].personagem.nome,
              sender: 'JOGO-DETETIVE'
      };

      $scope.messages.push(message);
    }

  });*/
});