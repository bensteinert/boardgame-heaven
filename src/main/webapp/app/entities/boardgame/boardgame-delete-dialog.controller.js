(function() {
    'use strict';

    angular
        .module('boardgameHeavenApp')
        .controller('BoardgameDeleteController',BoardgameDeleteController);

    BoardgameDeleteController.$inject = ['$uibModalInstance', 'entity', 'Boardgame'];

    function BoardgameDeleteController($uibModalInstance, entity, Boardgame) {
        var vm = this;

        vm.boardgame = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Boardgame.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
