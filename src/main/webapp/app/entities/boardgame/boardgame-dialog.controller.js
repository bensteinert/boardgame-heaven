(function() {
    'use strict';

    angular
        .module('boardgameHeavenApp')
        .controller('BoardgameDialogController', BoardgameDialogController);

    BoardgameDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Boardgame', 'Category', 'Player'];

    function BoardgameDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Boardgame, Category, Player) {
        var vm = this;

        vm.boardgame = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.categories = Category.query();
        vm.players = Player.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.boardgame.id !== null) {
                Boardgame.update(vm.boardgame, onSaveSuccess, onSaveError);
            } else {
                Boardgame.save(vm.boardgame, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('boardgameHeavenApp:boardgameUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, boardgame) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        boardgame.image = base64Data;
                        boardgame.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
