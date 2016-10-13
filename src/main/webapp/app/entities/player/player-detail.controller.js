(function() {
    'use strict';

    angular
        .module('boardgameHeavenApp')
        .controller('PlayerDetailController', PlayerDetailController);

    PlayerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Player', 'User', 'Boardgame'];

    function PlayerDetailController($scope, $rootScope, $stateParams, previousState, entity, Player, User, Boardgame) {
        var vm = this;

        vm.player = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('boardgameHeavenApp:playerUpdate', function(event, result) {
            vm.player = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
