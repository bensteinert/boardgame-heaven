'use strict';

describe('Controller Tests', function() {

    describe('Boardgame Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBoardgame, MockCategory, MockPlayer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBoardgame = jasmine.createSpy('MockBoardgame');
            MockCategory = jasmine.createSpy('MockCategory');
            MockPlayer = jasmine.createSpy('MockPlayer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Boardgame': MockBoardgame,
                'Category': MockCategory,
                'Player': MockPlayer
            };
            createController = function() {
                $injector.get('$controller')("BoardgameDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'boardgameHeavenApp:boardgameUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
