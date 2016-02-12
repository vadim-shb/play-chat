'use strict';

angular.module('webClient').controller('ChatRoomController', ['$scope', '$state', '$stateParams', 'wsCommunicator', function($scope, $state, $stateParams, wsCommunicator) {
    $scope.room = $stateParams.room;
    $scope.user = $stateParams.user;

    $scope.message = {
        message: ''
    };

    $scope.roomMessages = [];

    wsCommunicator.setReceiveMessageCallback(function(messageObj) {
        $scope.roomMessages.push({
            color: messageObj.author === $scope.user ? 'blue' : 'black',
            text: '[' + messageObj.author + ']: ' + messageObj.message
        });
        $scope.$apply(); //fixme: incapsulate digest in wsCommunicator
    });

    wsCommunicator.setCloseConnectionCallback(function() {
       console.log('connection closed')
    });

    wsCommunicator.connect(window.location.host, $scope.room, $scope.user);

    $scope.sendMessage = function() {
        wsCommunicator.sendMessage($scope.message);
        $scope.message = ''; //fixme: if message not receive?
    }

}]);