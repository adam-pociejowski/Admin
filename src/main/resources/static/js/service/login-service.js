app.factory('loginService', ['$http', function ($http) {

    return {
        login: function (credentials) {
            return $http.post('login', $.param(credentials), {
                headers : {
                    "content-type" : "application/x-www-form-urlencoded"
                }
            });
        },
        logout: function () {
            return $http.get('logout');
        },
        authenticate: function () {
            return $http.get('user');
        }
    };
}]);