'use strict';

angular.module('webClient').controller('ChatRoomController', ['$scope', '$state', '$stateParams', 'wsCommunicator', function($scope, $state, $stateParams, wsCommunicator) {
    $scope.room = $stateParams.room;
    $scope.user = $stateParams.user;

    $scope.message = {
        message: ''
    };

    $scope.roomMessages = [];

    wsCommunicator.setReceiveMessageCallback(function(messageObj) {
        roomMessages.push(messageObj);
    });

    wsCommunicator.connect('localhost:9000', $scope.room, $scope.user);

    $scope.sendMessage = function() {
        wsCommunicator.sendMessage($scope.message);
    }

}]);