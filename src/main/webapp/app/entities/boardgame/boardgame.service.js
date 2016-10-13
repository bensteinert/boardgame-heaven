(function() {
    'use strict';
    angular
        .module('boardgameHeavenApp')
        .factory('Boardgame', Boardgame);

    Boardgame.$inject = ['$resource'];

    function Boardgame ($resource) {
        var resourceUrl =  'api/boardgames/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
