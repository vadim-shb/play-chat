'use strict';

angular.module('webClient').controller('ChatRoomController', ['$scope', '$state', '$stateParams', 'wsCommunicator', function($scope, $state, $stateParams, wsCommunicator) {
    $scope.message = {
        room: $stateParams.room,
        user: $stateParams.user
    };

    console.log('==================================================');
    console.log($scope.message);
    console.log('==================================================');
    //$state.go('/entrance');
}]);