'use strict';

angular.module('webClient').controller('EntranceController', ['$scope', '$state', 'wsCommunicator', function($scope, $state, wsCommunicator) {
    $scope.checkEnterPossible = function() {
        return $scope.user && $scope.room;
    };

    $scope.enterRoom = function() {
        if ($scope.user && $scope.room) {
            //todo: may be ws enter signal. think later.
            $state.go('chatRoom', {user: $scope.user ,room: $scope.room});
        }
    };
}]);