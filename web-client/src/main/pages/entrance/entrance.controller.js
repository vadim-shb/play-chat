'use strict';

angular.module('webClient').controller('EntranceController', ['$scope', '$state', 'wsCommunicator', function($scope, $state, wsCommunicator) {
    $scope.checkEnterPossible = function() {
        return $scope.user && $scope.room;
    };

    $scope.onEnter = function() {
        if ($scope.user && $scope.room) {
            //todo: may be ws enter signal. think later.
            $state.go('/chat-room/user/' + $scope.user + '/room/' + $scope.room)
        }
    };
}]);