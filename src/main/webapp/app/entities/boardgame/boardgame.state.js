(function() {
    'use strict';

    angular
        .module('boardgameHeavenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('boardgame', {
            parent: 'entity',
            url: '/boardgame?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'boardgameHeavenApp.boardgame.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/boardgame/boardgames.html',
                    controller: 'BoardgameController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('boardgame');
                    $translatePartialLoader.addPart('difficulty');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('boardgame-detail', {
            parent: 'entity',
            url: '/boardgame/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'boardgameHeavenApp.boardgame.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/boardgame/boardgame-detail.html',
                    controller: 'BoardgameDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('boardgame');
                    $translatePartialLoader.addPart('difficulty');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Boardgame', function($stateParams, Boardgame) {
                    return Boardgame.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'boardgame',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('boardgame-detail.edit', {
            parent: 'boardgame-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/boardgame/boardgame-dialog.html',
                    controller: 'BoardgameDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Boardgame', function(Boardgame) {
                            return Boardgame.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('boardgame.new', {
            parent: 'boardgame',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/boardgame/boardgame-dialog.html',
                    controller: 'BoardgameDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                minNumberOfPlayers: null,
                                maxNumberOfPlayers: null,
                                minDuration: null,
                                maxDuration: null,
                                minAgePlayer: null,
                                difficulty: null,
                                releaseYear: null,
                                rating: null,
                                description: null,
                                image: null,
                                imageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('boardgame', null, { reload: 'boardgame' });
                }, function() {
                    $state.go('boardgame');
                });
            }]
        })
        .state('boardgame.edit', {
            parent: 'boardgame',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/boardgame/boardgame-dialog.html',
                    controller: 'BoardgameDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Boardgame', function(Boardgame) {
                            return Boardgame.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('boardgame', null, { reload: 'boardgame' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('boardgame.delete', {
            parent: 'boardgame',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/boardgame/boardgame-delete-dialog.html',
                    controller: 'BoardgameDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Boardgame', function(Boardgame) {
                            return Boardgame.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('boardgame', null, { reload: 'boardgame' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
