(function () {
    'use strict';

    angular
        .module('boardgameHeavenApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
