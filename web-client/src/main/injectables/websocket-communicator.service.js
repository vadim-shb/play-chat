'use strict';

angular.module('webClient').factory('wsCommunicator', function() {

    var ws;
    var callbacks = {};

    function connect(address, room, user) {
        ws = new WebSocket('ws://' + address + '/api/ws');
        ws.onmessage = function(evt) {
            var message = JSON.parse(evt.data);
            if (message.msgType == 'BROADCAST_TEXT_MESSAGE' && typeof callbacks.receiveMessage === 'function') {
                callbacks.receiveMessage(JSON.parse(message.data));
            }
        };
        setTimeout(function() {
            ws.send(JSON.stringify({
                msgType: 'HANDSHAKE',
                data: JSON.stringify({
                    room: room,
                    user: user
                })
            }));
        }, 300); //fixme: hardcode 1 sec is hack. Optimize it later
    }

    function setReceiveMessageCallback(callback) {
        callbacks.receiveMessage = callback;
    }

    function setCloseConnectionCallback(callback) {
        callbacks.close = callback;
        if (ws) {
            ws.onclose = callback;
        }
    }

    function disconnect() {
        ws.close();
    }

    function sendMessage(messageObj) {
        ws.send(JSON.stringify({
            msgType: 'SEND_TEXT_MESSAGE',
            data: JSON.stringify(messageObj)
        }));
    }

    return {
        connect: connect,
        disconnect: disconnect,
        sendMessage: sendMessage,
        setReceiveMessageCallback: setReceiveMessageCallback,
        setCloseConnectionCallback: setCloseConnectionCallback
    }
});