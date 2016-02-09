'use strict';

angular.module('webClient').controller('ChatRoomController', ['$scope', '$state', 'wsCommunicator', function($scope, $state, wsCommunicator) {
    $scope.dd = osmMapService.k;
}]);