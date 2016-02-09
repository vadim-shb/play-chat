'use strict';

angular
    .module('webClient', [
        'ui.router'
    ])
    .config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/entrance');

        $stateProvider
            .state('entrance', {
                url: '/entrance',
                templateUrl: '/pages/entrance/entrance.html',
                controller: 'EntranceController'
            })
            .state('chatRoom', {
                url: '/chat-room/user/{user}/room/{room}',
                templateUrl: '/pages/chat-room/chat-room.html',
                controller: 'ChatRoomController'
            })
    }]);
