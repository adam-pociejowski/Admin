app.factory('healthCheckerService', ['$http', function ($http) {

    return {
        getLastReport: function () {
            return $http.get('/healthchecker/rest/getlastreport');
        }
    };
}]);