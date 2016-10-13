(function() {
    'use strict';

    angular
        .module('boardgameHeavenApp')
        .controller('BoardgameDetailController', BoardgameDetailController);

    BoardgameDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Boardgame', 'Category', 'User'];

    function BoardgameDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Boardgame, Category, User) {
        var vm = this;

        vm.boardgame = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('boardgameHeavenApp:boardgameUpdate', function(event, result) {
            vm.boardgame = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
