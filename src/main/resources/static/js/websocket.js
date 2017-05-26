var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

function connect() {
    var socket = new SockJS('/websocket-poc');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/chat', function (message) {
            showMessage(JSON.parse(message.body));
        });

        stompClient.subscribe('/user/queue/chat', function (message) {
            // showMessage(JSON.parse(message.body));
        });

        stompClient.subscribe('/topic/activeUsers', function (users) {
            // clearUsers();
            // updateUsers(JSON.parse(users.body));
        });

        stompClient.subscribe('/user/queue/command', function (command) {
            doCommand(JSON.parse(command.body));
        });

        //getUsers();

        getCommand();
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/message", {},
        JSON.stringify({
            'text': $("#chat-message").val()
            ,'recipient': ''
        })
    );
    $("#chat-message").val("");
    $("#chat-message").focus();
}

function sendCharacter() {
    console.log("Caracter selecionado:" + $("#characters-select").val());
    stompClient.send("/app/command", {},
        JSON.stringify({
            'type': 'CHOOSE_CHARACTER',
            'response': $("#characters-select").val()
        })
    );
}

function getUsers() {
    stompClient.send("/app/users", {}, '');
}

function getCommand() {
    stompClient.send("/app/command", {},
        JSON.stringify({
                'type': 'FIRST_COMMAND'
        })
    );
}

function showMessage(message) {
    if (message.sender == null) {
        $("#chat_corpo").append("<tr><td><strong>" + message.text + "</strong></td></tr>");
    } else {
        $("#chat_corpo").append("<tr><td><strong>" + message.sender +
            "</strong> says: " + message.text + "</td></tr>");
    }
}

function doCommand(command) {
    if (command.type == "CHOOSE_CHARACTER") {
        choiceCharacterPopup(command);
    }
    if (command.type == "UPDATE_CHARACTER") {
         updateCharacter(command);
    }
    console.log('command: ' + command);
}

function choiceCharacterPopup(command) {
    var characters = command.options;
    characters.forEach(function(item) {
        var option = document.createElement('option');
        option.value = item.name;
        option.innerHTML = item.name;
        document.getElementById("characters-select").appendChild(option);
    });
    $("#choice-characters-modal").modal("show");
}

function updateCharacter(command) {
    $(".avatar ng-bi")
    var characters = command.options;
    characters.forEach(function(item) {
        var option = document.createElement('option');
        option.value = item.name;
        option.innerHTML = item.name;
        document.getElementById("characters-select").appendChild(option);
    });
    $("#choice-characters-modal").modal("show");
}

function clearUsers() {
    document.getElementById('recipient').innerHTML = "";
}

function updateUsers(users) {

    function addAllPeople() {
        var option = document.createElement('option');
        option.value = '';
        option.innerHTML = 'All people';
        document.getElementById("recipient").appendChild(option);
    }

    addAllPeople();
    users.forEach(function(item) {
        var option = document.createElement('option');
        option.value = item;
        option.innerHTML = item;
        document.getElementById("recipient").appendChild(option);
    });
}

$(function () {
/*
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    */
    $( "#send-character" ).click(function() { sendCharacter(); $('#choice-characters-modal').modal('hide');});
    $( "#send-message" ).click(function() { sendMessage(); });

    connect();
});
